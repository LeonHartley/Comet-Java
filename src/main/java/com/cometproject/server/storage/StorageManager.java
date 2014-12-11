package com.cometproject.server.storage;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.utilities.Initializable;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.log4j.Logger;


public class StorageManager implements Initializable {
    private static StorageManager storageManagerInstance;
    private static Logger log = Logger.getLogger(StorageManager.class.getName());
    private HikariDataSource connections = null;

    public StorageManager() {

    }

    @Override
    public void initialize() {
        boolean isConnectionFailed = false;

        try {
            String[] connectionDetails = Comet.getServer().getConfig().get("comet.db.host").split(":");

            HikariConfig config = new HikariConfig();
            config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            config.addDataSourceProperty("serverName", connectionDetails[0]);
            config.addDataSourceProperty("port", connectionDetails.length > 1 ? Integer.parseInt(connectionDetails[1]) : 3306);
            config.addDataSourceProperty("databaseName", Comet.getServer().getConfig().get("comet.db.name"));
            config.addDataSourceProperty("user", Comet.getServer().getConfig().get("comet.db.username"));
            config.addDataSourceProperty("password", Comet.getServer().getConfig().get("comet.db.password"));
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

        SqlHelper.init(this);
    }

    public static StorageManager getInstance() {
        if (storageManagerInstance == null)
            storageManagerInstance = new StorageManager();

        return storageManagerInstance;
    }

    public HikariDataSource getConnections() {
        return this.connections;
    }
}
