package com.cometproject.server.storage.queue;

import com.cometproject.server.tasks.CometTask;
import com.cometproject.api.utilities.Initialisable;

public interface StorageQueue<T> extends Initialisable, CometTask {

    void queueSave(T object);

    void unqueue(T object);

    boolean isQueued(T object);

    void shutdown();
}
