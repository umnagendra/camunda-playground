package xyz.nagendra.camundaplayground.api;

import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkflowController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowController.class);

    private final RuntimeService runtimeService;

    @Autowired
    public WorkflowController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @GetMapping("/start-workflow/{workflow_key}")
    public ResponseEntity<Object> startWorkflow(@PathVariable("workflow_key") String workflowKey) {
        LOGGER.info("Received GET request to start workflow: {}", workflowKey);

        // The process instance must be an asynchronously instantiated by adding
        // 'camunda:asyncBefore' extension attribute on a process-level start event.
        //
        // Else, the entire process execution will block the client (web request) thread.
        runtimeService.startProcessInstanceByKey(workflowKey);
        return ResponseEntity.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"status\": \"started\"}");
    }
}
