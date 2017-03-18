package com.cometproject.website.api;

import com.cometproject.website.config.Configuration;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

import java.util.concurrent.Future;

public class FbApiClient {
    private final Gson gson = new Gson();

    public static final String FB_API_VERSION = "v2.8";

    public static final String FB_APP_ID = "1260936063961905";
    private static final String FB_APP_SECRET = "5f61f7d4968939964875152251ce7884";

    private final AsyncHttpClient httpClient = new AsyncHttpClient();

    public FbApiClient() {

    }

    public String getAccessToken(final String code) {
        String accessToken = "";

        try {
            Future<Response> responseFuture = this.httpClient.prepareGet("https://graph.facebook.com/" + FB_API_VERSION + "/oauth/access_token")
                    .addQueryParameter("client_id", FB_APP_ID)
                    .addQueryParameter("client_secret", FB_APP_SECRET)
                    .addQueryParameter("code", code)
                    .addQueryParameter("scope", "email")
                    .addQueryParameter("redirect_uri", Configuration.getInstance().getSiteUrl() + "/facebook")
                    .execute();

            Response res = responseFuture.get();

            if (res.getResponseBody().startsWith("{")) {
                // the response is json
                final JsonObject object = this.gson.fromJson(res.getResponseBody(), JsonObject.class);

                return object.get("access_token").getAsString();
            } else {
                accessToken = res.getResponseBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (accessToken.startsWith("access_token=")) {
            accessToken = accessToken.substring(13);
        }

        return accessToken.split("&")[0];
    }


    public JsonObject getAccountData(final String accessToken) {

        try {
            Future<Response> responseFuture = this.httpClient.prepareGet("https://graph.facebook.com/" + FB_API_VERSION + "/me")
                    .addQueryParameter("access_token", accessToken)
                    .addQueryParameter("fields", "id,email,name")
                    .execute();

            Response res = responseFuture.get();

            return new Gson().fromJson(res.getResponseBody(), JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static final FbApiClient client = new FbApiClient();

    public static FbApiClient getClient() {
        return client;
    }
}
