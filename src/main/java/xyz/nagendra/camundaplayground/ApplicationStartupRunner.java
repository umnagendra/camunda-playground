package xyz.nagendra.camundaplayground;

import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartupRunner.class);
    private static final String WORKFLOW_KEY = "StockPriceEmailWorkflow";
    private static final String BUSINESS_KEY = "AUTO";

    private final RuntimeService runtimeService;

    @Autowired
    public ApplicationStartupRunner(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Application is initialized. Starting default workflow ...");

        long existingProcessInstanceCount = getProcessInstanceCount(WORKFLOW_KEY, BUSINESS_KEY);
        LOGGER.info("Existing process instance count = {}", existingProcessInstanceCount );

        if (existingProcessInstanceCount > 0) {
            LOGGER.error("CONFLICT: An active process instance with definition key = {}, business key = {} already exists in the system.",
                    WORKFLOW_KEY, BUSINESS_KEY);
            return;
        }

        runtimeService.startProcessInstanceByKey(WORKFLOW_KEY, BUSINESS_KEY);
    }

    private long getProcessInstanceCount(String workflowKey, String businessKey) {
        return runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(workflowKey)
                .processInstanceBusinessKey(businessKey)
                .active().count();
    }
}
