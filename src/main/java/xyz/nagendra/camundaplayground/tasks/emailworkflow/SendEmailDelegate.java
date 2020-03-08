package xyz.nagendra.camundaplayground.tasks.emailworkflow;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDateTime;

import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_HTML_REPORT;

@Component
public class SendEmailDelegate implements JavaDelegate {

    private static final String EMAIL_SUBJECT = "Stock Quotes Report - {0}";
    private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailDelegate.class);

    private final JavaMailSender javaMailSender;

    @Value("${email.toAddresses}")
    private String toAddresses;

    @Value("${email.replyTo}")
    private String replyToAddress;

    @Autowired
    public SendEmailDelegate(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Inside SendEmailDelegate ...");
        String subject = MessageFormat.format(EMAIL_SUBJECT, LocalDateTime.now());

        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(msg, StandardCharsets.UTF_8.name());

        mimeMessageHelper.setTo(toAddresses.split(","));
        mimeMessageHelper.setReplyTo(replyToAddress);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText((String)execution.getVariable(VAR_NAME_HTML_REPORT), true);

        LOGGER.info("Sending HTML report as email to {} with Subject '{}'", toAddresses, subject);

        try {
            javaMailSender.send(msg);
        } catch (Exception e) {
            LOGGER.error("Failed to send email to {}", toAddresses, e);
            throw new BpmnError("Failed to send email to " + toAddresses,
                    e.getClass().getCanonicalName() + ": " + e.getMessage());
        }

        LOGGER.info("SendEmailDelegate done");
    }
}
