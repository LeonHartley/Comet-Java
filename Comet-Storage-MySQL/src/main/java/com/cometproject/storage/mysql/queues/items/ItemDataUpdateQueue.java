package com.cometproject.storage.mysql.queues.items;

import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.MySQLStorageQueue;

import java.sql.PreparedStatement;
import java.util.concurrent.ScheduledExecutorService;

public class ItemDataUpdateQueue extends MySQLStorageQueue<Long, String> {

    public ItemDataUpdateQueue(long delayMilliseconds, ScheduledExecutorService executorService, MySQLConnectionProvider connectionProvider) {
        super("UPDATE items SET extra_data = ? WHERE id = ?;", delayMilliseconds, executorService, connectionProvider);
    }

    @Override
    public void processBatch(PreparedStatement preparedStatement, Long id, String object) throws Exception {
        preparedStatement.setString(1, object);
        preparedStatement.setLong(2, id);

        preparedStatement.addBatch();
    }
}
