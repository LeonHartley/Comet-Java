package com.cometproject.website.articles;

import com.cometproject.website.storage.dao.articles.ArticleDao;
import com.cometproject.website.storage.dao.players.PlayerDao;
import com.cometproject.website.utilities.DateUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Article {
    private int id;
    private String slug;
    private String title;
    private String description;
    private String body;
    private int author;
    private String promoImage;
    private boolean allowComments = false;
    private int timeCreated;

    public Article(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.slug = data.getString("article_slug");
        this.title = data.getString("article_title");
        this.description = data.getString("article_description");
        this.body = data.getString("article_body");
        this.author = data.getInt("article_author");
        this.promoImage = data.getString("article_promo_image");
        this.allowComments = data.getBoolean("article_allow_comments");
        this.timeCreated = data.getInt("article_time_created");
    }

    public Article() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public String getFormattedBody() {
        return this.body.replace("\n", "<br />");
    }

    public int getAuthor() {
        return author;
    }

    public String getPromoImage() {
        return promoImage;
    }

    public boolean isAllowComments() {
        return allowComments;
    }

    public int getTimeCreated() {
        return timeCreated;
    }

    public String getFormattedTimeCreated() {
        return DateUtil.format(this.timeCreated);
    }

    public String getAuthorName() {
        return PlayerDao.getUsernameById(this.author);
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public void setPromoImage(String promoImage) {
        this.promoImage = promoImage;
    }

    public void setAllowComments(boolean allowComments) {
        this.allowComments = allowComments;
    }

    public void setTimeCreated(int timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void save() {
        ArticleDao.save(this);
    }
}
