package com.cometproject.server.storage.queries;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatisticsDao {
    public static void updateStats(int players, int rooms, String version) throws SQLException {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = Comet.getServer().getStorage().getConnections().getConnection();
            connection.setAutoCommit(true);

            statement = connection.prepareStatement("UPDATE server_status SET "
                    + "active_players = " + Comet.getServer().getNetwork().getSessions().getUsersOnlineCount() + ","
                    + "active_rooms = " + GameEngine.getRooms().getActiveRooms().size() + ","
                    + "server_version = '" + Comet.getBuild() + "'");

        } catch(SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            if(connection != null && !connection.isClosed()) {
                connection.close();
            }

            if(statement != null && !statement.isClosed()) {
                statement.close();
            }
        }

    }
}
