package xyz.nagendra.camundaplayground.tasks.emailworkflow;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.nagendra.camundaplayground.tasks.emailworkflow.model.GlobalQuote;
import xyz.nagendra.camundaplayground.tasks.emailworkflow.model.StockQuote;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STOCK_QUOTES_LIST;

@Component
public class StockQuotesDelegate implements JavaDelegate {

    private static final String STOCK_API_URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol={0}&apikey={1}";
    private static final Logger LOGGER = LoggerFactory.getLogger(StockQuotesDelegate.class);

    @Value("${alphavantage.apikey:API_KEY_MISSING}")
    private String apiKey;

    @Value("${stock.symbols:CSCO}")
    private String symbolsCSV;

    private final Gson gson = new Gson();

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Inside StockQuotesDelegate ...");

        List<String> symbols = Arrays.asList(symbolsCSV.split(","));
        execution.setVariable(VAR_NAME_STOCK_QUOTES_LIST, getStockQuotes(symbols));

        LOGGER.info("StockQuotesDelegate done");
    }

    private List<StockQuote> getStockQuotes(List<String> symbols) {
        return symbols.stream().map(this::getStockQuote).collect(Collectors.toList());
    }

    private StockQuote getStockQuote(String symbol) {
        LOGGER.info("Requesting stock quote for {} ...", symbol);

        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url(getAPIURLForSymbol(symbol))
                    .get()
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = Objects.requireNonNull(response.body()).string();
                LOGGER.info("URL is {}", getAPIURLForSymbol(symbol));

                if (response.isSuccessful()) {
                    LOGGER.info("Received stock quote for {}: {}", symbol, responseBody);
                    return gson.fromJson(responseBody, GlobalQuote.class).getStockQuote();
                } else {
                    throw new IOException("API request to get stock quote for " + symbol +" failed. Response status = " + response.code() + ",  body = {}" + responseBody);
                }

            }
        } catch (Exception e) {
            LOGGER.error("Failed to get stock quote for {}", symbol, e);
            throw new BpmnError("Failed to get stock quote for " + symbol,
                    e.getClass().getCanonicalName() + ": " + e.getMessage());
        }
    }

    private String getAPIURLForSymbol(String symbol) {
        return MessageFormat.format(STOCK_API_URL, symbol, apiKey);
    }
}
