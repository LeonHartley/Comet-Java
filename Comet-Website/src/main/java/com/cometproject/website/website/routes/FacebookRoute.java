package com.cometproject.website.website.routes;

import com.cometproject.website.api.FbApiClient;
import com.cometproject.website.config.Configuration;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FacebookRoute {
    public static String facebookLogin(Request req, Response res) {

        if (req.queryParams("code") != null) {
            // Let's log the user in
            final String code = req.queryParams("code");
            final String accessToken = FbApiClient.getClient().getAccessToken(code);

            if (accessToken == null) {
                return "Invalid access token";
            }

            final JsonObject accountData = FbApiClient.getClient().getAccountData(accessToken);

            if (accountData != null) {
                final String id = accountData.get("id").getAsString();
                final String name = accountData.get("name").getAsString();

                final String email = accountData.has("email") ? accountData.get("email").getAsString() : null;

                if(email == null) {
                    // create account with no email, prompt to enter one

                }

                // prompt them to also pick a name.

                // Find someone in the database with the account ID, log them in etc. If they don't exist, we need to
                // add them to the database and log them in
                return "hi, " + name + ".. your account id is " + id;
            }

            return "failed to login!";
        }

        try {
            res.redirect("http://www.facebook.com/dialog/oauth?" + "client_id="
                    + FbApiClient.FB_APP_ID + "&redirect_uri="
                    + URLEncoder.encode(Configuration.getInstance().getSiteUrl() + "/facebook", "UTF-8")
                    + "&scope=email");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
