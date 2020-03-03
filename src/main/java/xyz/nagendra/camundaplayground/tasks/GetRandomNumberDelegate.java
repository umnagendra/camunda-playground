package xyz.nagendra.camundaplayground.tasks;

import io.vavr.control.Try;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static xyz.nagendra.camundaplayground.Constants.*;

@Component
public class GetRandomNumberDelegate implements JavaDelegate {

    private static final String RANDOM_ORG_URL = "https://www.random.org/integers/?num=1&min=1&max=1000&col=1&base=10&format=plain&rnd=new";

    private static final Logger LOGGER = LoggerFactory.getLogger(GetRandomNumberDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        LOGGER.info("Inside GetRandomNumberDelegate ... started by {} @ {}",
                delegateExecution.getVariable(VAR_NAME_STARTED_BY), delegateExecution.getVariable(VAR_NAME_STARTED_AT));
        getRandomNumber()
                .onSuccess(num -> {
                    boolean isEven = (num % 2 == 0);
                    LOGGER.info("Num = {}, isEven = {}", num, isEven);

                    delegateExecution.setVariable(VAR_NAME_NUM, num);
                    delegateExecution.setVariable(VAR_NAME_IS_EVEN, isEven);
                })
                .onFailure(throwable -> {
                    LOGGER.error("Failed to get random number from random.org", throwable);
                    throw new BpmnError("Request failed to " + RANDOM_ORG_URL,
                            throwable.getClass().getCanonicalName() + ": " + throwable.getMessage());
                });

        LOGGER.info("GetRandomNumberDelegate done");
    }

    private Try<Integer> getRandomNumber() {
        return Try.of(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(RANDOM_ORG_URL)
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = Objects.requireNonNull(response.body()).string().trim();
                return Integer.valueOf(responseBody);
            }
        });
    }
}
