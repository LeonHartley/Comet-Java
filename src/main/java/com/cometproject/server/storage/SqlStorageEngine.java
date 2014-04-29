package com.cometproject.server.storage;

import com.cometproject.server.boot.Comet;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import org.apache.log4j.Logger;

import java.sql.*;

public class SqlStorageEngine {
    private static Logger log = Logger.getLogger(SqlStorageEngine.class.getName());
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
        return this.connections.getTotalLeased();
    }

    public BoneCP getConnections() {
        return this.connections;
    }

    public PreparedStatement prepare(String query) throws SQLException {
        return prepare(query, false);
    }

    public PreparedStatement prepare(String query, boolean returnKeys) throws SQLException {
        Connection conn = null;

        try {
            conn = this.connections.getConnection();

            if (returnKeys) {
                return conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            } else {
                return conn.prepareStatement(query);
            }

        } catch (SQLException e) {
            log.error("Error while creating prepared statement", e);
        } finally {
            conn.close();
        }

        return null;
    }

    public void execute(String query) {
        try {
            this.prepare(query).execute();
        } catch (SQLException e) {
            log.error("Error while executing MySQL query", e);
        }
    }

    public boolean exists(String query) throws SQLException {
        Connection conn = null;

        try {
            conn = this.connections.getConnection();
            return conn.createStatement().executeQuery(query).next();
        } catch (SQLException e) {
            log.error("Error while executing MySQL query", e);
        } finally {
            conn.close();
        }

        return false;
    }

    public int count(String query) throws SQLException {
        Connection conn = null;

        try {
            conn = this.connections.getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            return this.count(statement);
        } catch (SQLException e) {
            log.error("Error while creating prepared statement", e);
        } finally {
            conn.close();
        }

        return 0;
    }

    public int count(PreparedStatement statement) {
        int i = 0;

        try {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                i++;
            }

            return i;
        } catch (SQLException e) {
            log.error("Error while counting entries", e);
        }

        return 0;
    }

    public ResultSet getRow(String query) throws SQLException {
        Connection conn = null;
        try {
            conn = this.connections.getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                return result;
            }
        } catch (SQLException e) {
            log.error("Error while getting row", e);
        } finally {
            conn.close();
        }

        return null;
    }

    public ResultSet getTable(String query) throws SQLException {
        Connection conn = null;
        try {
            conn = this.connections.getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet result = statement.executeQuery();

            return result;
        } catch (SQLException e) {
            log.error("Error while getting table", e);
        } finally {
            conn.close();
        }

        return null;
    }

    public String getString(String query) {
        try {
            ResultSet result = this.prepare(query).executeQuery();
            result.first();

            String str = query.split(" ")[1];

            if (str.startsWith("`")) {
                str = str.substring(1, str.length() - 1);
            }

            return result.getString(str);
        } catch (SQLException e) {
            log.error("Error while getting string", e);
        }

        return null;
    }

    public void checkDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            Comet.exit("The JDBC driver is missing.");
        }
    }
}
