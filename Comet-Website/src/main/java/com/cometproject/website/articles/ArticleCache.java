package com.cometproject.website.articles;

import com.cometproject.website.storage.dao.articles.ArticleDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ArticleCache {
    private Map<Integer, Article> articles;
    private Map<String, Integer> articleSlugToId;

    public ArticleCache() {
        articles = new ConcurrentHashMap<>();
        articleSlugToId = new ConcurrentHashMap<>();

       this.refresh();
    }

    public void refresh() {
        ArticleDao.getAll(articles, articleSlugToId);
    }

    public List<Article> getLatestArticles(int count) {
        List<Article> articles = new ArrayList<>();

        for(Article article : this.articles.values()) {
            if(articles.size() == count) break;

            articles.add(article);
        }

        return articles;
    }

    public Map<Integer, Article> getArticles() {
        return articles;
    }

    public Map<String, Integer> getArticleSlugToId() {
        return articleSlugToId;
    }

    private static ArticleCache articleCache;

    public static ArticleCache getInstance() {
        if(articleCache == null)
            articleCache = new ArticleCache();

        return articleCache;
    }
}
