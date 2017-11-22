package com.cometproject.server.api;

import com.cometproject.api.config.CometSettings;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

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
        return savePhoto(data, roomId + "");
    }

    public String savePhoto(final byte[] data, String photoId) {
        try {
            Future<Response> responseFuture = asyncHttpClient.preparePost(CometSettings.cameraUploadUrl.replace("%photoId%", photoId))
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
