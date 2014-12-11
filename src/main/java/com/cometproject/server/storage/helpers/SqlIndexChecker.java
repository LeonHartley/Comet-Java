package com.cometproject.server.storage.helpers;

import com.cometproject.server.storage.StorageManager;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SqlIndexChecker {
    private static Logger log = Logger.getLogger(SqlIndexChecker.class);

    public static void checkIndexes(StorageManager engine) {
        log.debug("Checking for valid database indexes..");

        checkPlayersTable(engine);

        log.debug("Index check complete");
    }

    public static void setIndexes(StorageManager engine) {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = engine.getConnections().getConnection();
            stmt = con.prepareStatement("SHOW INDEX FROM `items`;");
            rs = stmt.executeQuery();

            while (rs.next()) {
                if (rs.getString("Column_name").equals("user_id")) {
                    log.debug("Setting Items Index Column Successfully!");
                    InventoryDao.ITEMS_USERID_INDEX = rs.getString("Key_name");
                    break;
                }
            }
        } catch (SQLException e) {

        } finally {
            try {
                con.close();
                stmt.close();
                rs.close();
            } catch (Exception e) {
            }
        }
    }

    private static void checkPlayersTable(StorageManager engine) {
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
            } catch (Exception e) {
            }
        }

        if (!hasIdIndex) {
            log.warn("Missing index in 'players' table for 'id' column");
        }

        if (!hasPlayerIndex) {
            log.warn("Missing index in 'players' table for 'id' column");
        }
    }
}
