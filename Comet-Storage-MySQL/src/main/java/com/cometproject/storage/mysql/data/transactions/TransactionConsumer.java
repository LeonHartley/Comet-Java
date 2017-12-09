package com.cometproject.storage.mysql.data.transactions;

public interface TransactionConsumer {
    void accept(Transaction transaction) throws Exception;
}
