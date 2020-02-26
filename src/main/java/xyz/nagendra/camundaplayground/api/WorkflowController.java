package xyz.nagendra.camundaplayground.api;

import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class WorkflowController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowController.class);

    private final RuntimeService runtimeService;

    @Autowired
    public WorkflowController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @GetMapping("/start-workflow")
    public ResponseEntity<Object> startWorkflow() {
        LOGGER.info("Received GET request to start workflow ...");
        CompletableFuture.runAsync(() -> runtimeService.startProcessInstanceByKey("RandomWorkflow"));
        return ResponseEntity.accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"status\": \"started\"}");
    }
}
