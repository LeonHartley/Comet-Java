package com.cometproject.website.storage.dao.articles;

import com.cometproject.website.articles.Article;
import com.cometproject.website.players.Player;
import com.cometproject.website.settings.SiteSettings;
import com.cometproject.website.storage.dao.DaoHelper;

import java.sql.*;
import java.util.Map;

public class ArticleDao {
    public static void getAll(Map<Integer, Article> articles, Map<String, Integer> slugMap) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("SELECT * FROM website_articles WHERE article_hidden = 'false' ORDER BY article_time_created DESC");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final Article newsArticle = new Article(resultSet);
                articles.put(newsArticle.getId(), newsArticle);
                slugMap.put(newsArticle.getSlug(), newsArticle.getId());
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }
    }

    public static void save(Article article) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("UPDATE website_articles SET article_slug = ?, article_title = ?, article_description = ?, article_body = ?, article_promo_image = ?, article_allow_comments = ?, article_time_created = ? WHERE id = ?");

            preparedStatement.setString(1, article.getSlug());
            preparedStatement.setString(2, article.getTitle());
            preparedStatement.setString(3, article.getDescription());
            preparedStatement.setString(4, article.getBody());
            preparedStatement.setString(5, article.getPromoImage());
            preparedStatement.setString(6, article.isAllowComments() ? "true" : "false");
            preparedStatement.setInt(7, article.getTimeCreated());

            preparedStatement.setInt(8, article.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }
    }

    public static int create(Article article) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement(
                    "INSERT INTO website_articles (article_slug, article_title, article_description, article_body, article_author, article_promo_image, article_allow_comments, article_time_created) VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, article.getSlug());
            preparedStatement.setString(2, article.getTitle());
            preparedStatement.setString(3, article.getDescription());
            preparedStatement.setString(4, article.getBody());
            preparedStatement.setInt(5, article.getAuthor());
            preparedStatement.setString(6, article.getPromoImage());
            preparedStatement.setString(7, article.isAllowComments() ? "true" : "false");
            preparedStatement.setInt(8, article.getTimeCreated());

            preparedStatement.execute();

            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return 0;
    }

    public static void hideArticle(int articleId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("UPDATE website_articles SET article_hidden = 'true' WHERE id = ?");

            preparedStatement.setInt(1, articleId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }
    }
}
