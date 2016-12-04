package com.cometproject.website.website.routes;

import com.cometproject.website.api.ApiClient;
import com.cometproject.website.pages.Page;
import com.cometproject.website.players.Player;
import com.cometproject.website.players.PlayerFollowMode;
import com.cometproject.website.players.PlayerPreferences;
import com.cometproject.website.storage.dao.players.PlayerDao;
import com.cometproject.website.utilities.PasswordUtil;
import com.cometproject.website.website.WebsiteManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class AccountRoute {
    private final static Page ACCOUNT_PAGE = new Page("account", "Account Settings", "me");

    public static ModelAndView index(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        model.put("page", ACCOUNT_PAGE);
        model.put("action", (req.params("action") == null) ? "preferences" : req.params("action"));

        if (req.session().attribute("player") != null)
            model.put("player", PlayerDao.getById(req.session().attribute("player")));

        if (req.session().attribute("success") != null) {
            model.put("success", true);
            req.session().attribute("success", null);
        }

        if(req.session().attribute("error") != null) {
            model.put("error", req.session().attribute("error"));
            req.session().attribute("error", null);
        }

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/account.vm");
    }

    public static ModelAndView disconnected(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        model.put("page", "disconnected");
        model.put("action", "disconnected");

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/account.vm");
    }

    public static Object submit(Request req, Response res) {
        String action = req.params("action");

        if (action == null) res.redirect("/account");

        switch (action) {
            case "preferences": {
                return submitPreferences(req, res);
            }

            case "password": {
                return submitPassword(req, res);
            }
        }

        return "Error";
    }

    private static Object submitPreferences(Request req, Response res) {
        Player player = PlayerDao.getById(req.session().attribute("player"));

        if (player == null) {
            res.redirect("/");
            return null;
        }

        String motto = req.queryParams("motto");

        boolean showOnlineStatus = Boolean.parseBoolean(req.queryParams("showOnlineStatus"));
        String followFriendMode = req.queryParams("followFriendMode");
        boolean friendRequestsAllowed = Boolean.parseBoolean(req.queryParams("friendRequestsAllowed"));

        if (motto == null) {
            req.session().attribute("formError", "fields");
            res.redirect("/account/preferences");
            return null;
        }

        player.setMotto(motto);

        PlayerPreferences preferences = player.getPreferences();

        preferences.setAllowFriendRequests(friendRequestsAllowed);
        preferences.setShowOnlineStatus(showOnlineStatus);
        preferences.setFollowMode(PlayerFollowMode.valueOf(followFriendMode.toUpperCase()));

        player.save();
        preferences.save();

        ApiClient.getInstance().execute("/player/" + player.getId() + "/reload");

        req.session().attribute("success", true);
        res.redirect("/account/preferences");
        return null;
    }

    private static Object submitPassword(Request req, Response res) {
        Player player = PlayerDao.getById(req.session().attribute("player"));

        if (player == null) {
            res.redirect("/");
            return null;
        }

        final String currentPassword = req.queryParams("currentPassword");
        final String newPassword = req.queryParams("newPassword");
        final String newPasswordConfirm = req.queryParams("newPasswordConfirm");

        if(currentPassword == null || newPassword == null || newPasswordConfirm == null) {
            res.redirect("/account/password");
            return null;
        }

        if(!PasswordUtil.hash(currentPassword).equals(player.getPassword())) {
            req.session().attribute("error", "Password does not match the password we have on record");
            res.redirect("/account/password");
            return null;
        }

        if(!newPassword.equals(newPasswordConfirm)) {
            req.session().attribute("error", "The passwords you entered do not match");
            res.redirect("/account/password");
            return null;
        }

        player.setPassword(PasswordUtil.hash(newPassword));
        player.save();

        req.session().attribute("success", true);
        res.redirect("/account/password");
        return null;
    }
}
