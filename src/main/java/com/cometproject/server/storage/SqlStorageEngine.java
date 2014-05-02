package com.cometproject.server.storage;

import com.cometproject.server.boot.Comet;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import org.apache.log4j.Logger;

public class SqlStorageEngine {
    private static Logger log = Logger.getLogger(SqlStorageEngine.class.getName());
    //private BoneCP connections = null;
    private BoneCP connections = null;

    public SqlStorageEngine() {
        checkDriver();

        boolean isConnectionFailed = false;

        try {
            BoneCPConfig config = new BoneCPConfig();

            config.setJdbcUrl("jdbc:mysql://" + Comet.getServer().getConfig().get("comet.db.host") + "/" + Comet.getServer().getConfig().get("comet.db.name"));
            config.setUsername(Comet.getServer().getConfig().get("comet.db.username"));
            config.setPassword(Comet.getServer().getConfig().get("comet.db.password"));

            config.setMinConnectionsPerPartition(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.min")));
            config.setMaxConnectionsPerPartition(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.max")));
            config.setPartitionCount(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.count")));

            log.info("Connecting to the MySQL server");
            this.connections = new BoneCP(config);

            /*String[] connectionDetails = Comet.getServer().getConfig().get("comet.db.host").split(":");

            HikariConfig config = new HikariConfig();
            config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            config.addDataSourceProperty("serverName", connectionDetails[0]);
            config.addDataSourceProperty("port", connectionDetails.length > 1 ? Integer.parseInt(connectionDetails[1]) : 3306);
            config.addDataSourceProperty("databaseName", Comet.getServer().getConfig().get("comet.db.name"));
            config.addDataSourceProperty("user", Comet.getServer().getConfig().get("comet.db.username"));
            config.addDataSourceProperty("password", Comet.getServer().getConfig().get("comet.db.password"));
            config.setMaximumPoolSize(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.max")));
            //config.setLeakDetectionThreshold(10000);

            this.connections = new HikariDataSource(config);*/
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

    public int getConnectionCount() {
        return 0;
    }

    public BoneCP getConnections() {
        return this.connections;
    }

    public void checkDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            Comet.exit("The JDBC driver is missing.");
        }
    }
}
