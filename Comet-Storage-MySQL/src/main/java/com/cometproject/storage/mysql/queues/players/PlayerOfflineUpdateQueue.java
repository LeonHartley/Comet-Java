package com.cometproject.storage.mysql.queues.players;

import com.cometproject.storage.mysql.MySQLConnectionProvider;
import com.cometproject.storage.mysql.MySQLStorageQueue;

import java.sql.PreparedStatement;
import java.util.concurrent.ScheduledExecutorService;

public class PlayerOfflineUpdateQueue extends MySQLStorageQueue<Integer, Object> {

    public PlayerOfflineUpdateQueue(long delayMilliseconds, ScheduledExecutorService executorService, MySQLConnectionProvider connectionProvider) {
        super("UPDATE players SET online = '0' WHERE id = ?;", delayMilliseconds, executorService, connectionProvider);
    }

    @Override
    public void processBatch(PreparedStatement preparedStatement, Integer id, Object object) throws Exception {
        preparedStatement.setInt(1, id);

        preparedStatement.addBatch();
    }
}
