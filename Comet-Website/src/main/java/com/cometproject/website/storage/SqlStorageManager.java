package com.cometproject.website.storage;

import com.cometproject.website.config.Configuration;
import com.cometproject.website.storage.dao.DaoHelper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;

public class SqlStorageManager {
    private static Logger log = Logger.getLogger(SqlStorageManager.class.getName());
    private HikariDataSource connections = null;

    public SqlStorageManager() {
        boolean isConnectionFailed = false;

        try {
            String[] connectionDetails = Configuration.getInstance().getDbHost().split(":");

            HikariConfig config = new HikariConfig();

            config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            config.addDataSourceProperty("serverName", connectionDetails[0]);
            config.addDataSourceProperty("port", connectionDetails.length > 1 ? Integer.parseInt(connectionDetails[1]) : 3306);
            config.addDataSourceProperty("databaseName", Configuration.getInstance().getDbDatabase());
            config.addDataSourceProperty("user", Configuration.getInstance().getDbUsername());
            config.addDataSourceProperty("password", Configuration.getInstance().getDbPassword());
            config.setMaximumPoolSize(Configuration.getInstance().getDbPoolSize());
            config.setLeakDetectionThreshold(300000);
            config.setInitializationFailFast(true);

            this.connections = new HikariDataSource(config);
            DaoHelper.init(this);
        } catch (Exception e) {
            isConnectionFailed = true;
            log.error("Failed to connect to MySQL server", e);
            System.exit(0);
        } finally {
            if (!isConnectionFailed) {
                log.info("Connection to MySQL server was successful");
            }
        }
    }

    public HikariDataSource getConnections() {
        return this.connections;
    }
}