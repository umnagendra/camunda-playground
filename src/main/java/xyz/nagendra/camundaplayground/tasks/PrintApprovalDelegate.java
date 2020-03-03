package xyz.nagendra.camundaplayground.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_APPROVED_BY;
import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STARTED_AT;
import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STARTED_BY;

@Component
public class PrintApprovalDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintApprovalDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Inside PrintApprovalDelegate ... started by = {} @ {}",
                execution.getVariable(VAR_NAME_STARTED_BY), execution.getVariable(VAR_NAME_STARTED_AT));
        LOGGER.info("Received approval from {} to get another random number. Proceeding ...", execution.getVariable(VAR_NAME_APPROVED_BY));
        LOGGER.info("PrintApprovalDelegate done");
    }
}
