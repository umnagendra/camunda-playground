package xyz.nagendra.camundaplayground.tasks.waituntilworkflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_FINISH_AT;
import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STARTED_AT;
import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STARTED_BY;

@Component
public class PrintFinishedDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintFinishedDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Inside PrintFinishedDelegate ... started by = {} @ {}",
                delegateExecution.getVariable(VAR_NAME_STARTED_BY), delegateExecution.getVariable(VAR_NAME_STARTED_AT));
        long finishAt = (Long) delegateExecution.getVariable(VAR_NAME_FINISH_AT);
        LOGGER.info("FinishAt = {} ({})", finishAt, new DateTime(finishAt));
        LOGGER.info("PrintFinishedDelegate done");
    }
}
