package xyz.nagendra.camundaplayground.api;

import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.nagendra.camundaplayground.api.model.WorkflowRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static xyz.nagendra.camundaplayground.Constants.*;

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
        LOGGER.info("Received POST request: {}", workflowRequest);

        // Business Keys need *NOT* be unique across multiple process instances
        // In fact, multiple process instances can be identified by a same business key
        // Therefore, we need explicit validation if we need to ensure that only one active
        // process instance runs for each business key.
        // See https://forum.camunda.org/t/how-does-business-key-works/2170/5
        if (runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(workflowRequest.getWorkflowKey())
                .processInstanceBusinessKey(workflowRequest.getBusinessKey())
                .active().count() > 0) {
            LOGGER.error("CONFLICT: Another active process instance with business key {} already exists in the system.", workflowRequest.getBusinessKey());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"status\": \"conflict\"}");
        }

        // The process instance must be an asynchronously instantiated by adding
        // 'camunda:asyncBefore' extension attribute on a process-level start event.
        //
        // Else, the entire process execution will block the client (web request) thread.
        runtimeService.startProcessInstanceByKey(workflowRequest.getWorkflowKey(), workflowRequest.getBusinessKey(), createProcessVariables(workflowRequest));
        return ResponseEntity.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"status\": \"started\"}");
    }

    private Map<String, Object> createProcessVariables(WorkflowRequest workflowRequest) {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put(VAR_NAME_STARTED_BY, workflowRequest.getStartedBy());
        varMap.put(VAR_NAME_STARTED_AT, LocalDateTime.now());
        return varMap;
    }
}

// TODO:
// handle approval flow for odd numbers
// get API - list of process instances waiting for approval (approvedBy, plus existing vars)
// single periodic workflow across clusters
