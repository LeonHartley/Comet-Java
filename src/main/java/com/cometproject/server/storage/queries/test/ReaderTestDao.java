package com.cometproject.server.storage.queries.test;

import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.storage.SqlHelper;
import com.cometproject.server.storage.collections.EmptyImmutableResultReader;
import com.cometproject.server.storage.collections.ImmutableResultReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Matty on 02/05/2014.
 */
public class ReaderTestDao {

    public static ImmutableResultReader loadPermissions() {
        // SELECT * FROM permission_perks

        Connection sqlConnection = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            PreparedStatement preparedStatement = SqlHelper.prepare("SELECT * FROM permission_perks", sqlConnection);
            ResultSet result = preparedStatement.executeQuery();

            return ImmutableResultReader.build(result);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(sqlConnection);
        }

        return ImmutableResultReader.empty();
    }
}
