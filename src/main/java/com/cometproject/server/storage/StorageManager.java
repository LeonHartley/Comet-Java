package com.cometproject.server.storage;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.utilities.Initializable;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;


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
            BoneCPConfig config = new BoneCPConfig();

            config.setJdbcUrl(
                    "jdbc:mysql://" + Comet.getServer().getConfig().get("comet.db.host") +
                    "/" + Comet.getServer().getConfig().get("comet.db.name") + "?tcpKeepAlive=" + Comet.getServer().getConfig().get("comet.db.pool.tcpKeepAlive") +
                    "&autoReconnect=" + Comet.getServer().getConfig().get("comet.db.pool.autoReconnect")
            );

            config.setUsername(Comet.getServer().getConfig().get("comet.db.username"));
            config.setPassword(Comet.getServer().getConfig().get("comet.db.password"));

            config.setMinConnectionsPerPartition(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.min")));
            config.setMaxConnectionsPerPartition(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.max")));
            config.setPartitionCount(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.count")));

            config.setIdleMaxAge(Integer.valueOf(Comet.getServer().getConfig().get("comet.db.pool.idleMaxAgeSeconds")), TimeUnit.SECONDS);
            config.setMaxConnectionAge(Integer.valueOf(Comet.getServer().getConfig().get("comet.db.pool.maxConnectionAgeSeconds")), TimeUnit.SECONDS);

            config.setAcquireRetryAttempts(Integer.valueOf(Comet.getServer().getConfig().get("comet.db.pool.acquireRetryAttempts")));

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
