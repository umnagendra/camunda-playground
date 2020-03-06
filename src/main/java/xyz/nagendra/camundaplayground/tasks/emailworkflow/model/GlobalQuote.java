package xyz.nagendra.camundaplayground.tasks.emailworkflow.model;

import com.google.gson.annotations.SerializedName;

// *** IMPORTANT ***
// Freemarker only works with public classes. It *does not* work if
// classes referenced are inner classes.
public class GlobalQuote {

    @SerializedName("Global Quote")
    private StockQuote stockQuote;

    public StockQuote getStockQuote() {
        return stockQuote;
    }
}
