package com.cometproject.storage.mysql;

import com.cometproject.api.utilities.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;

public abstract class MySQLStorageQueue<T, O> {

    private final String batchQuery;
    private final Map<T, O> storageQueue;
    private final Future processFuture;

    private boolean running = false;

    private final MySQLConnectionProvider connectionProvider;

    public MySQLStorageQueue(String batchQuery, long delayMilliseconds, ScheduledExecutorService executorService,
                             MySQLConnectionProvider connectionProvider) {
        this.batchQuery = batchQuery;
        this.storageQueue = new ConcurrentHashMap<>();

        this.connectionProvider = connectionProvider;
        this.processFuture = executorService.scheduleAtFixedRate(this::process,
                delayMilliseconds + (1000 * (ThreadLocalRandom.current().nextInt(1, 5 + 1))),
                delayMilliseconds, TimeUnit.MILLISECONDS);

    }

    public void add(T key, O obj) {
        if (this.storageQueue.containsKey(key)) {
            this.storageQueue.replace(key, obj);
        } else {
            this.storageQueue.put(key, obj);
        }
    }

    public void addAll(Collection<Pair<T, O>> all) {
        for (Pair<T, O> obj : all) {
            this.add(obj.getLeft(), obj.getRight());
        }
    }

    private void process() {
        this.running = true;

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = this.connectionProvider.getConnection();

            preparedStatement = sqlConnection.prepareStatement(this.batchQuery);

            for (Map.Entry<T, O> obj : this.storageQueue.entrySet()) {
                try {
                    this.processBatch(preparedStatement, obj.getKey(), obj.getValue());
                } catch (Exception e) {
                    this.onException(e);
                }
            }

            preparedStatement.executeBatch();

            this.storageQueue.clear();
        } catch (Exception e) {
            this.onException(e);
        } finally {
            this.connectionProvider.closeStatement(preparedStatement);
            this.connectionProvider.closeConnection(sqlConnection);
        }

        this.running = false;
    }

    public void onException(Exception e) {
        // TODO: log this somewhere useful, though it shouldn't really throw exceptions....
        e.printStackTrace();
    }

    public O getQueued(T obj) {
        return this.storageQueue.get(obj);
    }

    public void stop() {
        if (!this.running) {
            this.process();
        }

        this.processFuture.cancel(false);
    }

    protected abstract void processBatch(PreparedStatement preparedStatement, T id, O object) throws Exception;
}
