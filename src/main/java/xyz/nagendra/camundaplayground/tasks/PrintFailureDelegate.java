package xyz.nagendra.camundaplayground.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintFailureDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintFailureDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Inside PrintFailureDelegate ...");
        LOGGER.info("FAILURE! Received an odd number: {}", delegateExecution.getVariable("num"));
        LOGGER.info("PrintFailureDelegate done, but we're not stopping until we get an even number");
    }
}
