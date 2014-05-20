package com.cometproject.server.storage.helpers;

import com.cometproject.server.storage.SqlStorageEngine;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlIndexChecker {
    private static Logger log = Logger.getLogger(SqlIndexChecker.class);

    public static void checkIndexes(SqlStorageEngine engine) {
        log.info("Checking for valid database indexes..");

        checkPlayersTable(engine);

        log.info("Index check complete");
    }

    private static void checkPlayersTable(SqlStorageEngine engine) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        boolean hasIdIndex = false;
        boolean hasPlayerIndex = false;

        try {
            con = engine.getConnections().getConnection();
            stmt = con.prepareStatement("SHOW INDEX FROM `players`;");
            rs = stmt.executeQuery();

            while (rs.next()) {
                if (rs.getString("Column_name").equals("id")) {
                    hasIdIndex = true;
                } else if (rs.getString("Column_name").equals("username")) {
                    hasPlayerIndex = true;
                }
            }
        } catch (SQLException e) {

        } finally {
            try {
                con.close();
                stmt.close();
                rs.close();
            } catch (Exception e) { }
        }

        if (!hasIdIndex) {
            log.warn("Missing index in 'players' table for 'id' column");
        }

        if (!hasPlayerIndex) {
            log.warn("Missing index in 'players' table for 'id' column");
        }
    }
}
