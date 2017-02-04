package com.cometproject.website.api;

import com.cometproject.website.config.Configuration;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.ning.http.client.StringPart;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ApiClient {
    private AsyncHttpClient asyncHttpClient;
    private static final Logger log = Logger.getLogger(ApiClient.class.getName());

    // oh hahah
    private static final String RECAPTCHA_SECRET = "6LfDiv8SAAAAAKtNon-NPLdyRf4Rgro6FtT-dNGS";

    private boolean isOffline = false;

    public ApiClient() {
        this.asyncHttpClient = new AsyncHttpClient();
    }

    public JSONObject execute(String command) {
        return execute(command, null);
    }

    public JSONObject execute(String command, Map<String, String> headers) {
        if(this.isOffline) return new JSONObject();

        try {
            final AsyncHttpClient.BoundRequestBuilder builder = asyncHttpClient.prepareGet("http://" + Configuration.getInstance().getApiHost() + ":" + Configuration.getInstance().getApiPort() + command);

            if(headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    builder.addHeader(header.getKey(), header.getValue());
                }
            }

            Future<Response> responseFuture = builder.addHeader("authToken", Configuration.getInstance().getApiAuthToken()).execute();
            Response response = responseFuture.get();

            System.out.println(response.getResponseBody());
            return new JSONObject(response.getResponseBody());
        } catch (Exception e) {
            if(e instanceof ExecutionException) {
                // API is offline...
                this.isOffline = true;
            } else {
                // probably failed to connect or received invalid JSON data.
                log.error("Error while executing API request", e);
            }
        }

        return new JSONObject();
    }

    public boolean verifyCaptcha(String response, String ipAddress) {
        if(ipAddress.equals("0:0:0:0:0:0:0:1")) {
            ipAddress = "127.0.0.1";
        }

        try {
            Future<Response> responseFuture = asyncHttpClient.prepareGet("https://www.google.com/recaptcha/api/siteverify")
                    .addQueryParameter("secret", RECAPTCHA_SECRET)
                    .addQueryParameter("response", response)
                    .addQueryParameter("remoteip", ipAddress)
                    .execute();

            Response res = responseFuture.get();

            return res.getResponseBody().equals("{\n" +
                    "  \"success\": true\n" +
                    "}");
        } catch(Exception e) {
            return false;
        }
    }

    private static ApiClient apiClient;

    public static ApiClient getInstance() {
        if (apiClient == null)
            apiClient = new ApiClient();

        return apiClient;
    }
}
