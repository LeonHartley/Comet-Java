package com.cometproject.website.website.routes;

import com.cometproject.website.players.Player;
import com.cometproject.website.storage.dao.players.PlayerDao;
import com.cometproject.website.website.WebsiteManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class ClientRoute {
    public static ModelAndView index(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        final Player player = PlayerDao.getById(req.session().attribute("player"));

        model.put("player", player);
        model.put("loginTicket", player.generateTicket());

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/client.vm");
    }

    public static ModelAndView rapid(Request req, Response res) {
        final Map<String, Object> model = new HashMap<>();
        final Player player = PlayerDao.getById(req.session().attribute("player"));

        model.put("player", player);
        model.put("loginTicket", player.generateTicket());
        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/rapid.vm");
    }
}
