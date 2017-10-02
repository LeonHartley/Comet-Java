package com.cometproject.server.api;

import com.ning.http.client.*;

import java.util.concurrent.Future;

public class ApiClient {
    private static ApiClient apiClient;
    private AsyncHttpClient asyncHttpClient;
    private boolean isOffline = false;

    public ApiClient() {
        this.asyncHttpClient = new AsyncHttpClient();
    }

    public static ApiClient getInstance() {
        if (apiClient == null)
            apiClient = new ApiClient();

        return apiClient;
    }

    public String saveThumbnail(final byte[] data, int roomId) {
        try {
            Future<Response> responseFuture = asyncHttpClient.preparePost("http://localhost:8080/camera/upload/" + roomId)
                    .addHeader("Content-Type", "application/octet-stream")
                    .setBody(data)
                    .execute();

            Response res = responseFuture.get();

            return res.getResponseBody();
        } catch (Exception e) {
            return "";
        }
    }
}
