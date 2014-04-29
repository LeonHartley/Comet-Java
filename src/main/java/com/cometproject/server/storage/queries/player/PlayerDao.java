package com.cometproject.server.storage.queries.player;

import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Matty on 28/04/2014.
 */
public class PlayerDao {

    public static int getIdBySSO(String authTicket) {
        Connection sqlConnection = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            PreparedStatement preparedStatement = SqlHelper.prepare("SELECT `id` FROM players WHERE auth_ticket = ?", sqlConnection);
            preparedStatement.setString(1, authTicket);

            ResultSet r = preparedStatement.executeQuery();

            if (r.next()) {
                return r.getInt("id");
            }
        } catch (SQLException e) {
            // Central place to handle all sql exceptions (easy for logging them etc)
            SqlHelper.handleSqlException(e);
        } finally {
            // Helper handles all the necessary checks automatically
            SqlHelper.closeSilently(sqlConnection);
        }

        return -1;
    }

    public static PlayerData getDataById(int id) {
        Connection sqlConnection = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            PreparedStatement preparedStatement = SqlHelper.prepare("SELECT * FROM players WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, id);

            ResultSet result = SqlHelper.getRow(preparedStatement, sqlConnection);

            if (result == null) {
                return null;
            }

            return new PlayerData(result.getInt("id"), result.getString("username"), result.getString("motto"), result.getString("figure"), result.getString("gender"), result.getInt("rank"), result.getInt("credits"), result.getInt("vip_points"), result.getString("reg_date"), result.getInt("last_online"), result.getString("vip").equals("1"), result.getInt("achievement_points"));
        } catch (SQLException e) {
            // Central place to handle all sql exceptions (easy for logging them etc)
            SqlHelper.handleSqlException(e);
        } finally {
            // Helper handles all the necessary checks automatically
            SqlHelper.closeSilently(sqlConnection);
        }

        return null;
    }

    public static PlayerSettings getSettingsById(int id) {
        Connection sqlConnection = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            PreparedStatement preparedStatement = SqlHelper.prepare("SELECT * FROM player_settings WHERE player_id = ?", sqlConnection);
            preparedStatement.setInt(1, id);

            ResultSet result = SqlHelper.getRow(preparedStatement, sqlConnection);

            if (result == null) {
                SqlHelper.closeStatementSilently(preparedStatement);

                preparedStatement = SqlHelper.prepare("INSERT into player_settings (`player_id`) VALUES(?)", sqlConnection);
                preparedStatement.setInt(1, id);

                SqlHelper.executeStatementSilently(preparedStatement, false);
                return new PlayerSettings();
            }

            return new PlayerSettings(result);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(sqlConnection);
        }

        return new PlayerSettings();
    }

    public static PlayerStatistics getStatisticsById(int id) {
        Connection sqlConnection = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            PreparedStatement preparedStatement = SqlHelper.prepare("SELECT * FROM player_stats WHERE player_id = ?", sqlConnection);
            preparedStatement.setInt(1, id);

            ResultSet result = SqlHelper.getRow(preparedStatement, sqlConnection);

            if (result == null) {
                SqlHelper.closeStatementSilently(preparedStatement);

                preparedStatement = SqlHelper.prepare("INSERT into player_stats (`player_id`) VALUES(?)", sqlConnection);
                preparedStatement.setInt(1, id);

                SqlHelper.executeStatementSilently(preparedStatement, false);
                return new PlayerStatistics(id);
            }

            return new PlayerStatistics(result);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(sqlConnection);
        }

        return new PlayerStatistics(id);
    }
}
