package com.cometproject.server.storage.execution;

import com.cometproject.server.storage.queue.types.ItemStorageQueue;
import com.cometproject.server.storage.queue.types.PlayerDataStorageQueue;
import org.apache.log4j.Logger;

public class QueueExecutor extends Thread {

    private final Logger log = Logger.getLogger(QueueExecutor.class);

    public QueueExecutor() {
        super("Storage-Queue-Executor");
    }

    @Override
    public void run() {
        try {
            final long startTime = System.currentTimeMillis();

            PlayerDataStorageQueue.getInstance().run();
            ItemStorageQueue.getInstance().run();

            log.trace(String.format("Queue execution took %sms", System.currentTimeMillis() - startTime));

            Thread.sleep(1500);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
