package com.cometproject.website.website.routes.admin;

import com.cometproject.website.players.Player;
import com.cometproject.website.players.items.PlayerItem;
import com.cometproject.website.storage.dao.players.PlayerDao;
import com.cometproject.website.utilities.PasswordUtil;
import com.cometproject.website.website.WebsiteManager;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayersRoute {
    private static final Gson gson = new Gson();
    public static ModelAndView index(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        if(req.session().attribute("message") != null) {
            model.put("message", req.session().attribute("message"));
            req.session().removeAttribute("message");
        }

        if(req.session().attribute("error") != null) {
            model.put("error", req.session().attribute("error"));
            req.session().removeAttribute("error");
        }

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/admin/players.vm");
    }

    public static String search(Request req, Response res) {
        final Map<String, Object> data = new HashMap<>();
        final String username = req.queryParams("username");

        if(username.isEmpty()) {
            data.put("players", new ArrayList<>());
            return gson.toJson(data);
        }

        final List<Player> players = PlayerDao.searchByUsername(username);
        data.put("players", players);

        return gson.toJson(data);
    }

    public static Object password(Request req, Response res) {
        final int playerId = Integer.parseInt(req.params("playerId"));
        final String password = req.queryParams("password");
        final String confirmPassword = req.queryParams("password-confirm");

        if(!password.equals(confirmPassword)) {
            req.session().attribute("error", "The passwords you entered do not match!");
            res.redirect("/admin/players");
            return null;
        }

        final Player player = PlayerDao.getById(playerId);

        if(player != null) {
            player.setPassword(PasswordUtil.hash(password));
            player.save();
        }

        req.session().attribute("message", "Password saved successfully");
        res.redirect("/admin/players");
        return null;
    }

    public static Object data(Request req, Response res) {
        final int playerId = Integer.parseInt(req.params("playerId"));
        final String username = req.queryParams("username");
        final String figure = req.queryParams("figure");
        final String email = req.queryParams("email");
        final int rank = Integer.parseInt(req.queryParams("rank"));
        final int credits= Integer.parseInt(req.queryParams("credits"));
        final int vipPoints = Integer.parseInt(req.queryParams("vipPoints"));
        final int activityPoints = Integer.parseInt(req.queryParams("activityPoints"));

        final Player player = PlayerDao.getById(playerId);

        if(player != null) {
            player.setUsername(username);
            player.setFigure(figure);
            player.setEmail(email);
            player.setRank(rank);
            player.setCredits(credits);
            player.setVipPoints(vipPoints);
            player.setActivityPoints(activityPoints);

            player.save();
        }

        req.session().attribute("message", "Player saved successfully");
        res.redirect("/admin/players");
        return null;
    }


    public static String inventory(Request req, Response res) {
        final Map<String, Object> data = new HashMap<>();
        final String playerId = req.queryParams("playerId");

        if(playerId.isEmpty() || !StringUtils.isNumeric(playerId)) {
            data.put("inventory", new ArrayList<>());
            return gson.toJson(data);
        }

        final List<PlayerItem> inventory = PlayerDao.getInventory(Integer.parseInt(playerId));
        data.put("inventory", inventory);

        return gson.toJson(data);
    }
}
