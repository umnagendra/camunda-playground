package xyz.nagendra.camundaplayground.tasks.randomworkflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_NUM;
import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STARTED_AT;
import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STARTED_BY;

@Component
public class PrintFailureDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintFailureDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Inside PrintFailureDelegate ... started by = {} @ {}",
                delegateExecution.getVariable(VAR_NAME_STARTED_BY), delegateExecution.getVariable(VAR_NAME_STARTED_AT));
        LOGGER.info("FAILURE! Received an odd number: {}", delegateExecution.getVariable(VAR_NAME_NUM));
        LOGGER.info("PrintFailureDelegate done, but we're not stopping until we get an even number");
    }
}
