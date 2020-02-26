package xyz.nagendra.camundaplayground.tasks;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class GetRandomNumberDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetRandomNumberDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Inside GetRandomNumberDelegate ...");

        int num = getRandomNumber();
        boolean isEven = (num % 2 == 0);

        LOGGER.info("Num = {}, isEven = {}", num, isEven);
        delegateExecution.setVariable("num", num);
        delegateExecution.setVariable("isEven", isEven);
        LOGGER.info("GetRandomNumberDelegate done");
    }

    // TODO actually make an API call to get random number
    private int getRandomNumber() {
        return new Random().nextInt(10000);
    }
}
