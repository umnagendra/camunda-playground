package xyz.nagendra.camundaplayground;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RandomWorkflowTest {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Test
    public void startRandomWorkflow() throws InterruptedException {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("RandomWorkflow");
        assertNotNull(pi);
        // wait for some time so the process instance can complete
        Thread.sleep(3000);
        assertTrue(pi.isEnded());
    }
}
