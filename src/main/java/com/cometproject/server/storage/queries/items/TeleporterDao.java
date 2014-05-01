package com.cometproject.server.storage.queries.items;


import com.cometproject.server.storage.SqlHelper;
import com.cometproject.server.storage.collections.EmptyImmutableResultReader;
import com.cometproject.server.storage.collections.ImmutableResultReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Matty on 30/04/2014.
 */
public class TeleporterDao {

    public static ImmutableResultReader getTeleporterPartners(int itemId) {
        Connection sqlConnection = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            PreparedStatement statement = SqlHelper.prepare("SELECT * FROM items_teles WHERE id_one = ?", sqlConnection);
            statement.setInt(1, itemId);

            ImmutableResultReader reader = new ImmutableResultReader(statement.executeQuery(), false);
            return reader;
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(sqlConnection);
        }

        return new EmptyImmutableResultReader();
    }
}
