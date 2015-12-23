package com.cometproject.server.storage.queue;

import com.cometproject.server.tasks.CometTask;
import com.cometproject.server.utilities.Initializable;

public interface StorageQueue<T> extends Initializable, CometTask {

    void queueSave(T object);

    void unqueue(T object);

    boolean isQueued(T object);

    void shutdown();
}
