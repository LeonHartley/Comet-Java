package com.cometproject.storage.mysql.data.transactions;

import java.sql.Connection;

public interface Transaction {
    Transaction NULL = null;

    void startTransaction() throws Exception;

    Connection getConnection();

    void commit() throws Exception;

    void rollback() throws Exception;
}
