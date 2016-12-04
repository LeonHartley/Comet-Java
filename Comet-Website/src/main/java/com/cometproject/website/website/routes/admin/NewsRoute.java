package com.cometproject.website.website.routes.admin;

import com.cometproject.website.articles.Article;
import com.cometproject.website.articles.ArticleCache;
import com.cometproject.website.storage.dao.articles.ArticleDao;
import com.cometproject.website.website.WebsiteManager;
import org.apache.commons.lang.StringUtils;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class NewsRoute {
    public static ModelAndView index(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        if(req.session().attribute("success") != null) {
            model.put("success", true);
            req.session().removeAttribute("success");
        }

        model.put("articles", ArticleCache.getInstance().getArticles());

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/admin/news/news.vm");
    }

    public static ModelAndView edit(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();

        if(req.params("id") == null || !StringUtils.isNumeric(req.params("id"))) {
            res.redirect("/admin/news");
            return null;
        }

        final int articleId = Integer.parseInt(req.params("id"));
        final Article article = ArticleCache.getInstance().getArticles().get(articleId);

        if(article == null) {
            res.redirect("/admin/news");
            return null;
        }

        model.put("article", ArticleCache.getInstance().getArticles().get(articleId));

        return new ModelAndView(WebsiteManager.applyGlobals(model), "./templates/admin/news/editArticle.vm");
    }

    public static Route save(Request req, Response res) {
        if(req.params("id") == null || !StringUtils.isNumeric(req.params("id"))) {
            res.redirect("/admin/news");
            return null;
        }

        final int articleId = Integer.parseInt(req.params("id"));

        final String title = req.queryParams("title");
        final String slug = req.queryParams("slug");
        final String promoImage = req.queryParams("promoImage");
        final String description = req.queryParams("description");
        final String body = req.queryParams("body");

        if(title == null || slug == null || promoImage == null || description == null || body == null) {
            res.redirect("/admin/news");
            return null;
        }

        final Article article = ArticleCache.getInstance().getArticles().get(articleId);

        if(article == null) {
            res.redirect("/admin/news");
            return null;
        }

        article.setTitle(title);
        article.setSlug(slug);
        article.setPromoImage(promoImage);
        article.setDescription(description);
        article.setBody(body);

        article.save();

        req.session().attribute("success", true);
        res.redirect("/admin/news");
        return null;
    }

    public static Route create(Request req, Response res) {
        final String title = req.queryParams("title");
        final String slug = req.queryParams("slug");
        final String promoImage = req.queryParams("promoImage");
        final String description = req.queryParams("description");
        final String body = req.queryParams("body");

        if(title == null || slug == null || promoImage == null || description == null || body == null) {
            res.redirect("/admin/news");
            return null;
        }

        final Article article = new Article();

        article.setTitle(title);
        article.setSlug(slug);
        article.setPromoImage(promoImage);
        article.setDescription(description);
        article.setBody(body);
        article.setAuthor(req.session().attribute("player"));

        article.setTimeCreated((int) (System.currentTimeMillis() / 1000L));

        article.setId(ArticleDao.create(article));

        ArticleCache.getInstance().getArticles().put(article.getId(), article);

        req.session().attribute("success", true);
        res.redirect("/admin/news");
        return null;
    }

    public static Route delete(Request req, Response res) {
        if(req.params("id") == null || !StringUtils.isNumeric(req.params("id"))) {
            res.redirect("/admin/news");
            return null;
        }

        final int articleId = Integer.parseInt(req.params("id"));
        final Article article = ArticleCache.getInstance().getArticles().get(articleId);

        if(article == null) {
            res.redirect("/admin/news");
            return null;
        }

        ArticleDao.hideArticle(articleId);
        ArticleCache.getInstance().getArticles().remove(articleId);

        req.session().attribute("success", true);
        res.redirect("/admin/news");
        return null;
    }
}
