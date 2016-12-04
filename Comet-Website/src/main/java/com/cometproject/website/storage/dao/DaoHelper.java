package com.cometproject.website.storage.dao;

import com.cometproject.website.storage.SqlStorageManager;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DaoHelper {
    private static SqlStorageManager storageManager;
    private static Logger log = Logger.getLogger(DaoHelper.class.getName());

    public static void init(SqlStorageManager sqlStorageManager) {
        storageManager = sqlStorageManager;
    }

    public static Connection getConnection() {
        try {
            return storageManager.getConnections().getConnection();
        } catch(Exception e) {
            handleException(e);
        }

        return null;
    }

    public static void close(Object closableObject) {
        try {
            if (closableObject instanceof Connection) {
                ((Connection) closableObject).close();
            } else if (closableObject instanceof ResultSet) {
                ((ResultSet) closableObject).close();
            } else if(closableObject instanceof PreparedStatement) {
                ((PreparedStatement) closableObject).close();
            }
        } catch(Exception e) {
            handleException(e);
        }
    }


    public static void handleException(Exception e) {
        log.error("Exception thrown from DAO", e);
    }
}
