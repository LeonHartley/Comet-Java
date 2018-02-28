package com.cometproject.server.storage.queue;

import com.cometproject.api.utilities.Initialisable;
import com.cometproject.server.tasks.CometTask;

public interface StorageQueue<T> extends Initialisable, CometTask {

    void queueSave(T object);

    void unqueue(T object);

    boolean isQueued(T object);

    void shutdown();
}
