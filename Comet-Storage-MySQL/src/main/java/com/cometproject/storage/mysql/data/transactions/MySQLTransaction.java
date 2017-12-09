package com.cometproject.storage.mysql.data.transactions;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLTransaction implements Transaction {

    private final Connection connection;

    public MySQLTransaction(Connection connection) {
        this.connection = connection;
    }

    public void startTransaction() throws Exception {
        this.connection.setAutoCommit(false);
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    public void commit() throws Exception {
        this.connection.commit();
    }

    @Override
    public void rollback() throws Exception {
        this.connection.rollback();
    }
}
