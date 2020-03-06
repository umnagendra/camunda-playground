package xyz.nagendra.camundaplayground.tasks.emailworkflow;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import xyz.nagendra.camundaplayground.tasks.emailworkflow.model.StockQuote;

import java.util.Collections;
import java.util.List;

import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_HTML_REPORT;
import static xyz.nagendra.camundaplayground.Constants.VAR_NAME_STOCK_QUOTES_LIST;

@Component
public class HtmlReportDelegate implements JavaDelegate {

    private static final String TEMPLATE_DIR = "/";
    private static final String TEMPLATE_FILE = "stock_quotes_report.ftl";

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlReportDelegate.class);

    private final Configuration freemarkerConfig;

    @Autowired
    public HtmlReportDelegate(Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LOGGER.info("Inside HtmlReportDelegate ...");

        List<StockQuote> stockQuotes = (List<StockQuote>) execution.getVariable(VAR_NAME_STOCK_QUOTES_LIST);
        LOGGER.info("Received {} stock quotes for reporting", stockQuotes.size());

        // setup freemarker template
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_DIR);
        Template template = freemarkerConfig.getTemplate(TEMPLATE_FILE);

        // apply data to template and generate HTML
        String htmlString = FreeMarkerTemplateUtils.processTemplateIntoString(template, Collections.singletonMap("stockQuotes", stockQuotes));
        LOGGER.info("Generated HTML report:\n{}", htmlString);

        // set it as a variable in this execution (for subsequent task to process)
        execution.setVariable(VAR_NAME_HTML_REPORT, htmlString);

        LOGGER.info("HtmlReportDelegate done");
    }
}
