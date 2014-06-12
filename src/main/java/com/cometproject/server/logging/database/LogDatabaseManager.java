package com.cometproject.server.logging.database;

import com.cometproject.server.boot.Comet;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;

public class LogDatabaseManager {
    private static Logger log = Logger.getLogger(LogDatabaseHelper.class.getName());
    private HikariDataSource connections = null;

    public LogDatabaseManager() {
        boolean isConnectionFailed = false;

        try {
            String[] connectionDetails = Comet.getServer().getConfig().get("comet.game.logging.database.host").split(":");

            HikariConfig config = new HikariConfig();
            config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            config.addDataSourceProperty("serverName", connectionDetails[0]);
            config.addDataSourceProperty("port", connectionDetails.length > 1 ? Integer.parseInt(connectionDetails[1]) : 3306);
            config.addDataSourceProperty("databaseName", Comet.getServer().getConfig().get("comet.game.logging.database.name"));
            config.addDataSourceProperty("user", Comet.getServer().getConfig().get("comet.game.logging.database.username"));
            config.addDataSourceProperty("password", Comet.getServer().getConfig().get("comet.game.logging.database.password"));
            config.setMaximumPoolSize(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.max")));
            config.setLeakDetectionThreshold(300000);
            config.setInitializationFailFast(true);

            this.connections = new HikariDataSource(config);
        } catch (Exception e) {
            isConnectionFailed = true;
            log.error("Failed to connect to MySQL server", e);
            System.exit(0);
        } finally {
            if (!isConnectionFailed) {
                log.info("Connection to MySQL server was successful");
            }
        }

        LogDatabaseHelper.init(this);
    }

    public HikariDataSource getConnections() {
        return this.connections;
    }
}
