package com.cometproject.server.api;

import com.cometproject.api.config.CometSettings;
import com.cometproject.server.boot.Comet;
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
            String url = CometSettings.cameraUploadUrl.replace("%photoId%", photoId);
            Future<Response> responseFuture = asyncHttpClient.preparePost(url)
                    .addHeader("Content-Type", "application/octet-stream")
                    .setBody(data)
                    .execute();

            Response res = responseFuture.get();

            String response = res.getResponseBody();

            Comet.getServer().getLogger().info("camera response (url=" + url + ", statusCode=" + res.getStatusCode() + "): " + response);
            return response;
        } catch (Exception e) {
            Comet.getServer().getLogger().error("Error uploading image", e);
            return "";
        }
    }
}
