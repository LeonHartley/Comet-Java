package com.cometproject.website.storage.dao.gamedata;

import com.cometproject.website.storage.dao.DaoHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class GameDataDao {
    public static void getAllVariables(Map<String, String> variables) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("SELECT `key`, `value` FROM website_gamedata_variables");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                variables.put(resultSet.getString("key"), resultSet.getString("value"));
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }
    }

    public static void getAllTexts(Map<String, String> texts) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("SELECT `key`, `value` FROM website_gamedata_texts");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                texts.put(resultSet.getString("key"), resultSet.getString("value"));
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }
    }
}
