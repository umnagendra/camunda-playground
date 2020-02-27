package xyz.nagendra.camundaplayground;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class RandomWorkflowTest {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    /**
     * This is more an "integration test", rather than a unit test because
     * it executes the entire workflow, and we need to assert upon its completion
     *
     * @throws InterruptedException
     */
    @Test
    public void startRandomWorkflow() throws InterruptedException {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("RandomWorkflow");

        assertNotNull(pi);
        String processInstanceId = pi.getProcessInstanceId();

        // wait for some time so the process instance can complete
        // No idea why, but we've to wait for a long time to ensure the process is ended and flushed
        Thread.sleep(20000);

        // Ref. https://groups.google.com/forum/#!topic/camunda-bpm-users/d0qntahqqXI
        // If a process instance runs through till the end it is removed from the runtime database
        // and only exists in the history database afterwards.
        // So you have to query the process instance from the history to check if it is ended
        // (or check if a query on the runtime returns no result).
        assertNull(runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult());
    }
}
