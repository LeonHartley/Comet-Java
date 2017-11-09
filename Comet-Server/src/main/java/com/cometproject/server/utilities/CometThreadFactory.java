package com.cometproject.server.utilities;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CometThreadFactory implements ThreadFactory {

    private final String baseName;
    private final AtomicInteger threadCounter;

    public CometThreadFactory(String baseNameFormat) {
        this.baseName = baseNameFormat;
        this.threadCounter = new AtomicInteger(0);
    }

    @Override
    public Thread newThread(Runnable r) {
        int threadId = this.threadCounter.incrementAndGet();
        final Thread thread = new Thread(r, String.format("Comet-%s-%s", baseName, threadId));

        // Set prioritisation or whatever?

        return thread;
    }
}
