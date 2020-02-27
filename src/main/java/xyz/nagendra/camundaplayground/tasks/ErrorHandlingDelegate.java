package xyz.nagendra.camundaplayground.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STARTED_AT;
import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STARTED_BY;

@Component
public class ErrorHandlingDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlingDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Inside ErrorHandlingDelegate ... started by = {} @ {}",
                execution.getVariable(VAR_NAME_STARTED_BY), execution.getVariable(VAR_NAME_STARTED_AT));
        LOGGER.info("Error occurred. Code = {}, Message = {}", execution.getVariable("ERROR-OCCURED"), execution.getVariable("ERROR-MSG"));
    }
}
