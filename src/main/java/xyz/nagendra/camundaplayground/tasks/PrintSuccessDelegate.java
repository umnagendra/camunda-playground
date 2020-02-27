package xyz.nagendra.camundaplayground.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PrintSuccessDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintSuccessDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Inside PrintSuccessDelegate ...");
        LOGGER.info("SUCCESS! Received an even number: {}", delegateExecution.getVariable("num"));
        LOGGER.info("PrintSuccessDelegate done");
    }
}
