package com.cometproject.server.game.players.queue;

import com.cometproject.server.tasks.CometThreadManagement;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PlayerLoginQueueManager {
    private static final int WAIT_TIME = 2;

    private final PlayerLoginQueue loginQueue;
    private ScheduledFuture future;

    public PlayerLoginQueueManager(boolean autoStart, CometThreadManagement threadMgr) {
        this.loginQueue = new PlayerLoginQueue();
        if (autoStart) { this.start(threadMgr); }
    }

    private void start(CometThreadManagement threadMgr) {
        if (this.future != null) { return; }
        this.future = threadMgr.executePeriodic(this.loginQueue, 5, WAIT_TIME, TimeUnit.SECONDS);
    }

    public boolean queue(PlayerLoginQueueEntry entry) {
        return this.loginQueue.queue(entry);
    }
}
