package com.cometproject.website.website.routes;

import com.cometproject.website.articles.ArticleCache;
import com.cometproject.website.pages.Page;
import com.cometproject.website.storage.dao.players.PlayerDao;
import com.cometproject.website.website.WebsiteManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class MeRoute {
    private final static Page ME_PAGE = new Page("me", "Home", "me");

    public static ModelAndView index(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        model.put("page", ME_PAGE);

        if(req.session().attribute("player") != null)
            model.put("player", PlayerDao.getById(req.session().attribute("player")));

        model.put("articles", ArticleCache.getInstance().getLatestArticles(9));

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/me.vm");
    }
}
