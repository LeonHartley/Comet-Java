package com.cometproject.website.website.routes;

import com.cometproject.website.articles.Article;
import com.cometproject.website.articles.ArticleCache;
import com.cometproject.website.pages.Page;
import com.cometproject.website.storage.dao.players.PlayerDao;
import com.cometproject.website.website.WebsiteManager;
import org.apache.commons.lang.StringUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticlesRoute {
    private final static Page ARTICLES_PAGE = new Page("articles", "Articles", "community");

    public static ModelAndView index(Request req, Response res) {
        final Map<String, Object> model = new HashMap<>();

        model.put("page", ARTICLES_PAGE);

        if(req.session().attribute("player") != null)
            model.put("player", PlayerDao.getById(req.session().attribute("player")));

        model.put("articles", ArticleCache.getInstance().getLatestArticles(12));
        model.put("selectedArticle", 0);

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/articles.vm");
    }

    public static ModelAndView get(Request req, Response res) {
        final List<Article> articles = ArticleCache.getInstance().getLatestArticles(12);
        final Map<String, Object> model = new HashMap<>();

        int articleIndex = 0;

        if(req.params("id") != null) {
            final String id = req.params("id");
            final boolean isArticleId = StringUtils.isNumeric(id);

            for(Article article : articles) {
                if(isArticleId) {
                    if(article.getId() == Integer.parseInt(id)) {
                        articleIndex = articles.indexOf(article);
                    }
                } else {
                    if(article.getSlug().equals(id)) {
                        articleIndex = articles.indexOf(article);
                    }
                }
            }
        }

        model.put("page", ARTICLES_PAGE);

        if(req.session().attribute("player") != null)
            model.put("player", PlayerDao.getById(req.session().attribute("player")));

        model.put("articles", articles);
        model.put("selectedArticle", articleIndex);

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/articles.vm");
    }
}
