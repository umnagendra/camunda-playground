package xyz.nagendra.camundaplayground.api;

import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

        // The process instance must be an asynchronously instantiated by adding
        // 'camunda:asyncBefore' extension attribute on a process-level start event.
        //
        // Else, the entire process execution will block the client (web request) thread.
        runtimeService.startProcessInstanceByKey(workflowRequest.getWorkflowKey(), createProcessVariables(workflowRequest));
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
