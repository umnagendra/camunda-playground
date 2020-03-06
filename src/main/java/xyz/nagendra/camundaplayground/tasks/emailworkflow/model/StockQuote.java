package xyz.nagendra.camundaplayground.tasks.emailworkflow.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// *** IMPORTANT ***
// Freemarker only works with public classes. It *does not* work if
// classes referenced are inner classes.
public class StockQuote implements Serializable {

    @SerializedName("01. symbol")
    private String symbol;

    @SerializedName("05. price")
    private String price;

    @SerializedName("10. change percent")
    private String changePercent;

    public String getSymbol() {
        return symbol;
    }

    public String getPrice() {
        return price;
    }

    public String getChangePercent() {
        return changePercent;
    }
}
