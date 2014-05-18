package com.cometproject.server.game.players.queue;

import com.cometproject.server.tasks.CometThreadManagement;

public class StaticPlayerQueue {
    private static PlayerLoginQueueManager mgr;

    static {

    }

    public static void init(CometThreadManagement threadManagement) {
        mgr = new PlayerLoginQueueManager(true, threadManagement);
    }

    public static PlayerLoginQueueManager getQueueManager() {
        return mgr;
    }
}
