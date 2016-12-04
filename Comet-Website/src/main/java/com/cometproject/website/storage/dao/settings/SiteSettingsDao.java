package com.cometproject.website.storage.dao.settings;

import com.cometproject.website.settings.SiteSettings;
import com.cometproject.website.storage.dao.DaoHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SiteSettingsDao {
    public static SiteSettings get() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("SELECT * FROM website_config");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return new SiteSettings(resultSet);
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return new SiteSettings();
    }

    public static void save(SiteSettings siteSettings) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("UPDATE website_config SET hotel_name = ?, hotel_slogan = ?, hotel_description = ?, " +
                    "twitter_username = ?, facebook_username = ?, twitter_widget_id = ?, player_default_credits = ?, player_default_activity_points = ?, " +
                    "player_default_vip_points = ?, player_default_motto = ?, player_default_figure = ?, player_default_homeroom = ?, game_host = ?, " +
                    "game_port = ?, game_client_swf = ?, game_client_variables = ?, game_client_texts = ?, game_client_productdata = ?, game_client_furnidata = ?, " +
                    "game_client_base = ?");

            preparedStatement.setString(1, siteSettings.getHotelName());
            preparedStatement.setString(2, siteSettings.getHotelSlogan());
            preparedStatement.setString(3, siteSettings.getHotelDescription());
            preparedStatement.setString(4, siteSettings.getTwitterUsername());
            preparedStatement.setString(5, siteSettings.getFacebookUsername());
            preparedStatement.setString(6, siteSettings.getTwitterWidgetId());
            preparedStatement.setInt(7, siteSettings.getPlayerDefaultCredits());
            preparedStatement.setInt(8, siteSettings.getPlayerDefaultActivityPoints());
            preparedStatement.setInt(9, siteSettings.getPlayerDefaultVipPoints());
            preparedStatement.setString(10, siteSettings.getPlayerDefaultMotto());
            preparedStatement.setString(11, siteSettings.getPlayerDefaultFigure());
            preparedStatement.setInt(12, siteSettings.getPlayerDefaultHomeRoom());
            preparedStatement.setString(13, siteSettings.getGameHost());
            preparedStatement.setInt(14, siteSettings.getGamePort());
            preparedStatement.setString(15, siteSettings.getGameClientSwf());
            preparedStatement.setString(16, siteSettings.getGameClientVariables());
            preparedStatement.setString(17, siteSettings.getGameClientTexts());
            preparedStatement.setString(18, siteSettings.getGameClientProductData());
            preparedStatement.setString(19, siteSettings.getGameClientFurniData());
            preparedStatement.setString(20, siteSettings.getGameClientBase());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }
    }
}
