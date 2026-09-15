package usecase.stock_search;

import data.stock.AlphaVantage;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AlphaVantageTest {

    @Test
    void oneDayRangeUsesFreeDailyEndpoint() throws Exception {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    assertFalse(chain.request().url().queryParameter("function")
                            .contains("INTRADAY"));
                    String body = "{\"Meta Data\":{},"
                            + "\"Time Series (Daily)\":{"
                            + "\"2026-09-15\":{\"2. high\":\"101\","
                            + "\"3. low\":\"99\",\"4. close\":\"100\"}}}";
                    return new Response.Builder()
                            .code(200)
                            .message("OK")
                            .request(chain.request())
                            .protocol(Protocol.HTTP_1_1)
                            .body(ResponseBody.create(body,
                                    MediaType.get("application/json")))
                            .build();
                })
                .build();

        AlphaVantage api = new AlphaVantage(client);

        assertEquals(1, api.getTimeSeries("AAPL", "1D").size());
    }
}