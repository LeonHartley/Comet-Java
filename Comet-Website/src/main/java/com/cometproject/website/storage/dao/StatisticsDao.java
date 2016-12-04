package com.cometproject.website.storage.dao;

import com.cometproject.website.statistics.ServerStatistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticsDao {
    public static ServerStatistics get() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = DaoHelper.getConnection();

            preparedStatement = sqlConnection.prepareStatement("SELECT active_players, active_rooms, server_version FROM server_status");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return new ServerStatistics(resultSet);
            }
        } catch (SQLException e) {
            DaoHelper.handleException(e);
        } finally {
            DaoHelper.close(resultSet);
            DaoHelper.close(preparedStatement);
            DaoHelper.close(sqlConnection);
        }

        return null;
    }
}
