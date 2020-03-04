package xyz.nagendra.camundaplayground.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.pvm.runtime.ExecutionImpl;
import org.junit.jupiter.api.Test;
import xyz.nagendra.camundaplayground.tasks.randomworkflow.GetRandomNumberDelegate;

import static org.junit.jupiter.api.Assertions.*;

public class GetRandomNumberDelegateTest {

    @Test
    public void testExecute() throws Exception {
        DelegateExecution delegateExecution = new ExecutionImpl();
        GetRandomNumberDelegate task = new GetRandomNumberDelegate();
        task.execute(delegateExecution);
        boolean isEven = (boolean) delegateExecution.getVariable("isEven");
        int num = (int) delegateExecution.getVariable("num");

        assertTrue(num >= 0 && num < 10000);
    }
}
