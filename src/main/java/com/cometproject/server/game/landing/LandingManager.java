package com.cometproject.server.game.landing;

import com.cometproject.server.game.landing.types.PromoArticle;
import com.cometproject.server.storage.queries.landing.LandingDao;

import java.util.Map;

public class LandingManager {
    private Map<Integer, PromoArticle> articles;

    public LandingManager() {
        this.articles = LandingDao.getArticles();
    }

    public Map<Integer, PromoArticle> getArticles() {
        return articles;
    }
}
