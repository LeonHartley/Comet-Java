package com.cometproject.storage.mysql.data.transactions;

import java.sql.Connection;
import java.sql.SQLException;

public interface Transaction {
    Transaction NULL = null;

    void startTransaction() throws Exception;

    Connection getConnection();

    void commit() throws Exception;

    void rollback() throws Exception;
}
