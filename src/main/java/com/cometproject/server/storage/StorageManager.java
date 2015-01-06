package com.cometproject.server.storage;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.utilities.Initializable;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import org.apache.log4j.Logger;


public class StorageManager implements Initializable {
    private static StorageManager storageManagerInstance;
    private static Logger log = Logger.getLogger(StorageManager.class.getName());
    private BoneCP connections = null;

    public StorageManager() {

    }

    @Override
    public void initialize() {
        boolean isConnectionFailed = false;

        try {
//            String[] connectionDetails = Comet.getServer().getConfig().get("comet.db.host").split(":");

//            HikariConfig config = new HikariConfig();
//            config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
//            config.addDataSourceProperty("serverName", connectionDetails[0]);
//            config.addDataSourceProperty("port", connectionDetails.length > 1 ? Integer.parseInt(connectionDetails[1]) : 3306);
//            config.addDataSourceProperty("databaseName", Comet.getServer().getConfig().get("comet.db.name"));
//            config.addDataSourceProperty("user", Comet.getServer().getConfig().get("comet.db.username"));
//            config.addDataSourceProperty("password", Comet.getServer().getConfig().get("comet.db.password"));
//            config.setMaximumPoolSize(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.max")));
//
//            config.addDataSourceProperty("cachePrepStmts", "true");
//            config.addDataSourceProperty("prepStmtCacheSize", "250");
//            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//            config.addDataSourceProperty("useServerPrepStmts", "true");
//
//            config.setConnectionTimeout(1000);
//            config.setInitializationFailFast(true);

            BoneCPConfig config = new BoneCPConfig();

            config.setJdbcUrl("jdbc:mysql://" + Comet.getServer().getConfig().get("comet.db.host") + "/" + Comet.getServer().getConfig().get("comet.db.name"));
            config.setUsername(Comet.getServer().getConfig().get("comet.db.username"));
            config.setPassword(Comet.getServer().getConfig().get("comet.db.password"));

            config.setMinConnectionsPerPartition(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.min")));
            config.setMaxConnectionsPerPartition(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.max")));
            config.setPartitionCount(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.count")));

            log.info("Connecting to the MySQL server");
            this.connections = new BoneCP(config);
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

    public BoneCP getConnections() {
        return this.connections;
    }
}
