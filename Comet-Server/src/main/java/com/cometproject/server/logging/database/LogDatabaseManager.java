//package com.cometproject.server.logging.database;
//
//import com.cometproject.server.boot.Comet;
//import com.jolbox.bonecp.BoneCP;
//import com.jolbox.bonecp.BoneCPConfig;
//import org.apache.log4j.Logger;
//
//
//public class LogDatabaseManager {
//    private static Logger log = Logger.getLogger(LogDatabaseHelper.class.getName());
//    private BoneCP connections = null;
//
//    public LogDatabaseManager() {
//        boolean isConnectionFailed = false;
//
//        try {
//            BoneCPConfig config = new BoneCPConfig();
//
//            config.setJdbcUrl("jdbc:mysql://" + Comet.getServer().getConfig().get("comet.game.logging.database.host") + "/" + Comet.getServer().getConfig().get("comet.game.logging.database.name"));
//            config.setUsername(Comet.getServer().getConfig().get("comet.game.logging.database.username"));
//            config.setPassword(Comet.getServer().getConfig().get("comet.game.logging.database.password"));
//
//            config.setMinConnectionsPerPartition(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.min")));
//            config.setMaxConnectionsPerPartition(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.max")));
//            config.setPartitionCount(Integer.parseInt(Comet.getServer().getConfig().get("comet.db.pool.count")));
//
//            this.connections = new BoneCP(config);
//        } catch (Exception e) {
//            isConnectionFailed = true;
//            log.error("Failed to connect to MySQL server", e);
//            // TODO: Disable logging...
//        } finally {
//            if (!isConnectionFailed) {
//                log.info("Connection to MySQL server was successful");
//            }
//        }
//    }
//
//    public BoneCP getConnections() {
//        return this.connections;
//    }
//}
