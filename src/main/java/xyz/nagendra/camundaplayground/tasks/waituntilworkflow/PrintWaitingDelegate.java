package xyz.nagendra.camundaplayground.tasks.waituntilworkflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_FINISH_AT;

@Component
public class PrintWaitingDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintWaitingDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        long finishAt = (Long) delegateExecution.getVariable(VAR_NAME_FINISH_AT);
        Interval intervalRemaining = new Interval(System.currentTimeMillis(), finishAt);
        LOGGER.info("There's still ~ {} minutes ({} seconds) to go before we finish",
                intervalRemaining.toDuration().getStandardMinutes(), intervalRemaining.toDuration().getStandardSeconds());
    }
}
