package xyz.nagendra.camundaplayground.api;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.nagendra.camundaplayground.api.model.ApprovalRequest;
import xyz.nagendra.camundaplayground.api.model.WorkflowRequest;
import xyz.nagendra.camundaplayground.api.model.WorkflowResponse;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static xyz.nagendra.camundaplayground.Constants.TASK_ID_WAIT_APPROVAL;
import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_APPROVED_BY;
import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STARTED_AT;
import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STARTED_BY;

@RestController
public class WorkflowController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowController.class);

    private final RuntimeService runtimeService;

    @Autowired
    public WorkflowController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @PostMapping(value = "/start-workflow")
    public ResponseEntity<Object> startWorkflow(@RequestBody WorkflowRequest workflowRequest) {
        LOGGER.info("Received POST request to start workflow: {}", workflowRequest);

        String workflowKey = workflowRequest.getWorkflowKey();
        String businessKey = workflowRequest.getBusinessKey();

        // Business Keys need *NOT* be unique across multiple process instances
        // In fact, multiple process instances can be identified by a same business key
        // Therefore, we need explicit validation if we need to ensure that only one active
        // process instance runs for each business key.
        // See https://forum.camunda.org/t/how-does-business-key-works/2170/5
        if (getProcessInstanceCount(workflowKey, businessKey) > 0) {
            LOGGER.error("CONFLICT: An active process instance with definition key = {}, business key = {} already exists in the system.",
                    workflowKey, businessKey);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"status\": \"conflict\"}");
        }

        // The process instance must be an asynchronously instantiated by adding
        // 'camunda:asyncBefore' extension attribute on a process-level start event.
        //
        // Else, the entire process execution will block the client (web request) thread.
        runtimeService.startProcessInstanceByKey(workflowKey, businessKey, createProcessVariables(workflowRequest));
        return ResponseEntity.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"status\": \"started\"}");
    }

    @PostMapping(value = "/approve-workflow")
    public ResponseEntity<Object> approveWorkflow(@RequestBody ApprovalRequest approvalRequest) {
        LOGGER.info("Received POST request to approve workflow: {}", approvalRequest);

        String workflowKey = approvalRequest.getWorkflowKey();
        String businessKey = approvalRequest.getBusinessKey();

        // first, we check if the specific process instance exists
        if (getProcessInstanceCount(workflowKey, businessKey) == 0) {
            LOGGER.error("NOT FOUND: No active process instance found with definition key = {}, business key = {}",
                    workflowKey, businessKey);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"status\": \"not found\"}");
        }

        // then, we get the specific process instance waiting for approval
        ProcessInstance processInstance = getActiveProcessInstance(workflowKey, businessKey);
        Execution execution = runtimeService.createExecutionQuery()
                .processInstanceId(processInstance.getId())
                .activityId(TASK_ID_WAIT_APPROVAL)
                .singleResult();

        // and signal it (with variables) so it can proceed
        Map<String, Object> vars = new HashMap<>();
        vars.put(VAR_NAME_APPROVED_BY, approvalRequest.getApprovedBy());
        runtimeService.signal(execution.getId(), vars);

        return ResponseEntity.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"status\": \"approved\"}");
    }

    @GetMapping(value = "/workflow")
    public ResponseEntity<List<WorkflowResponse>> listProcessInstances() {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();
        List<WorkflowResponse> workflowResponses = processInstances.stream().map(processInstance -> new WorkflowResponse(processInstance.getProcessDefinitionId().split(":")[0], processInstance.getBusinessKey(), !processInstance.isEnded())).collect(Collectors.toList());
        return ResponseEntity.ok(workflowResponses);
    }

    @DeleteMapping(value = "/workflow/{workflowKey}/{businessKey}")
    public ResponseEntity<Object> deleteWorkflow(@NotNull @PathVariable String workflowKey, @PathVariable String businessKey) {
        LOGGER.info("Received DELETE request to delete workflow with definition key = {}, business key = {}",
                workflowKey, businessKey);

        // first, we check if the specific process instance exists
        if (getProcessInstanceCount(workflowKey, businessKey) == 0) {
            LOGGER.error("NOT FOUND: No active process instance found with definition key = {}, business key = {}",
                    workflowKey, businessKey);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"status\": \"not found\"}");
        }

        // get the ID of this process instance
        ProcessInstance processInstance = getActiveProcessInstance(workflowKey, businessKey);
        String pid = processInstance.getProcessInstanceId();

        // delete process instance
        LOGGER.info("Deleting process instance with ID {} ...", pid);
        runtimeService.deleteProcessInstance(pid, "DELETED BY API REQUEST");

        return ResponseEntity.ok().build();
    }

    private Map<String, Object> createProcessVariables(WorkflowRequest workflowRequest) {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put(VAR_NAME_STARTED_BY, workflowRequest.getStartedBy());
        varMap.put(VAR_NAME_STARTED_AT, LocalDateTime.now());
        return varMap;
    }

    private long getProcessInstanceCount(String workflowKey, String businessKey) {
        return runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(workflowKey)
                .processInstanceBusinessKey(businessKey)
                .active().count();
    }

    private ProcessInstance getActiveProcessInstance(String workflowKey, String businessKey) {
        return runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(workflowKey)
                .processInstanceBusinessKey(businessKey)
                .active().singleResult();
    }
}
