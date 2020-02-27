package xyz.nagendra.camundaplayground.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STARTED_AT;
import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STARTED_BY;

@Component
public class PrintSuccessDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintSuccessDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Inside PrintSuccessDelegate ... started by = {} @ {}",
                delegateExecution.getVariable(VAR_NAME_STARTED_BY), delegateExecution.getVariable(VAR_NAME_STARTED_AT));
        LOGGER.info("SUCCESS! Received an even number: {}", delegateExecution.getVariable("num"));
        LOGGER.info("PrintSuccessDelegate done");
    }
}
