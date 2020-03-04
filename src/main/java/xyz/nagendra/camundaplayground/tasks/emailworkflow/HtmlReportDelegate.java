package xyz.nagendra.camundaplayground.tasks.emailworkflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STOCK_QUOTES_LIST;

@Component
public class HtmlReportDelegate implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlReportDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Inside HtmlReportDelegate ...");

        List<StockQuotesDelegate.StockQuote> stockQuotes = (List<StockQuotesDelegate.StockQuote>) execution.getVariable(VAR_NAME_STOCK_QUOTES_LIST);
        LOGGER.info("Received {} stock quotes for reporting", stockQuotes.size());

        // TODO convert into HTML using FreeMarker

        LOGGER.info("HtmlReportDelegate done");
    }
}
