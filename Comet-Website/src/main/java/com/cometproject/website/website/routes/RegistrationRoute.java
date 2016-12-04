package com.cometproject.website.website.routes;

import com.cometproject.website.api.ApiClient;
import com.cometproject.website.config.Configuration;
import com.cometproject.website.players.Player;
import com.cometproject.website.settings.SiteSettings;
import com.cometproject.website.storage.dao.players.PlayerDao;
import com.cometproject.website.utilities.PasswordUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.validator.routines.EmailValidator;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class RegistrationRoute {
    private static final Gson gsonInstance = new Gson();

    public static Object submit(Request req, Response res) {
        final String username = req.queryParams("registrationBean.username");
        final String email = req.queryParams("registrationBean.email");
        final String password = req.queryParams("registrationBean.password");
        final String passwordConfirm = req.queryParams("registrationBean.password.confirm");
        final String recaptchaResponse = req.queryParams("g-recaptcha-response");

        final boolean isValidUsername = username != null && !username.isEmpty();
        final boolean isValidEmail = email != null && !email.isEmpty() && EmailValidator.getInstance().isValid(email);
        final boolean isValidPassword = (password != null && !password.isEmpty()) && (passwordConfirm != null && !passwordConfirm.isEmpty());

        final JsonObject jsonObject = new JsonObject();
        final Map<String, Object> registrationErrors = new HashMap<>();

        res.type("application/json");

        // TODO: Locale.

        if (!isValidUsername)
            registrationErrors.put("registration_username", "Please enter a valid username");

        if (!isValidEmail)
            registrationErrors.put("registration_email", "Please enter a valid email");

        if (!isValidPassword) {
            registrationErrors.put("registration_password", "Please enter a valid password");
            registrationErrors.put("registration_password_confirm", "Your password doesn't match!");
        } else if(!password.equals(passwordConfirm)) {
            registrationErrors.put("registration_password_confirm", "Your password doesn't match!");
        }

        /*if(!ApiClient.getInstance().verifyCaptcha(recaptchaResponse, req.ip())) {
            System.out.println(req.ip());
            registrationErrors.put("registration_captcha", "Invalid captcha");
        }*/

        if(isValidEmail) {
            final boolean isEmailAvailable = PlayerDao.isEmailAvailable(email);

            if(!isEmailAvailable) {
                registrationErrors.put("registration_email", "This email is already in use!");
            }
        }

        if(isValidUsername) {
            final boolean isUsernameAvailable = PlayerDao.isUsernameAvailable(username);

            if(!isUsernameAvailable) {
                registrationErrors.put("registration_username", "This username is already in use!");
            }
        }

        if(registrationErrors.size() != 0) {
            jsonObject.add("registrationErrors", gsonInstance.toJsonTree(registrationErrors));
            jsonObject.add("registrationMessages", new JsonObject());

            return gsonInstance.toJson(jsonObject);
        }

        Player player = PlayerDao.create(username, email, PasswordUtil.hash(password));

        if(player != null) {
            PlayerDao.createPreferences(player.getId(), SiteSettings.getInstance().getPlayerDefaultHomeRoom());

            req.session().attribute("player", player.getId());

            jsonObject.add("registrationCompletionRedirectUrl", gsonInstance.toJsonTree(Configuration.getInstance().getSiteUrl() + "/me"));
            jsonObject.add("registrationMessages", new JsonObject());
        }

        return gsonInstance.toJson(jsonObject);
    }
}
