package com.cometproject.website.website.routes;

import com.cometproject.website.pages.Page;
import com.cometproject.website.players.Player;
import com.cometproject.website.storage.dao.players.PlayerDao;
import com.cometproject.website.website.WebsiteManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class PlaylistRoute {
    private final static Page PLAYLIST_ROUTE = new Page("playlist", "Playlist Editor", "me");

    public static ModelAndView index(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        model.put("page", PLAYLIST_ROUTE);

        model.put("player", PlayerDao.getById(req.session().attribute("player")));
        model.put("playlist", ((Player) model.get("player")).getPlaylist());

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/playlistEditor.vm");
    }
}
