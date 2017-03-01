package com.cometproject.website.website.routes.admin;

import com.cometproject.website.players.Player;
import com.cometproject.website.storage.dao.players.PlayerDao;
import com.cometproject.website.website.WebsiteManager;
import com.google.gson.Gson;
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
}
