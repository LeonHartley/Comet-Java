package com.cometproject.server.storage.queries.player.messenger;

import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessengerSearchDao {
    public static List<PlayerData> performSearch(String query) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<PlayerData> data = new ArrayList<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            // escape wildcard strings
            query = SqlHelper.escapeWildcards(query);

            preparedStatement = SqlHelper.prepare("SELECT * FROM players WHERE username LIKE ? LIMIT 50;", sqlConnection);
            preparedStatement.setString(1, query + "%");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(new PlayerData(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("motto"), resultSet.getString("figure"), resultSet.getString("gender"), resultSet.getString("email") == null ? "" : resultSet.getString("email"), resultSet.getInt("rank"), resultSet.getInt("credits"), resultSet.getInt("vip_points"), resultSet.getString("reg_date"), resultSet.getInt("last_online"), resultSet.getString("vip").equals("1"), resultSet.getInt("achievement_points"), resultSet.getInt("reg_timestamp"), resultSet.getInt("favourite_group"), resultSet.getString("last_ip")));
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return data;
    }
}
