package xyz.nagendra.camundaplayground.tasks.emailworkflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendEmailDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Inside SendEmailDelegate ...");

        // TODO send email using SMTP

        LOGGER.info("SendEmailDelegate done");
    }
}
