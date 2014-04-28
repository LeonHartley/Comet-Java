package com.cometproject.server.storage.queries.player;

import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Matty on 28/04/2014.
 */
public class UserDao {

    public static int getIdBySSO(String authTicket) {
        Connection sqlConnection = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            PreparedStatement preparedStatement = SqlHelper.prepare("SELECT `id` FROM players WHERE auth_ticket = ?", sqlConnection);
            preparedStatement.setString(1, authTicket);

            ResultSet r = preparedStatement.executeQuery();
            return r.getInt("id");
        } catch (SQLException e) {
            // Central place to handle all sql exceptions (easy for loggign them etc)
            SqlHelper.handleSqlException(e);
        } finally {
            // Helper handles all the necessary checks automatically
            SqlHelper.closeSilently(sqlConnection);
        }

        return -1;
    }
}
