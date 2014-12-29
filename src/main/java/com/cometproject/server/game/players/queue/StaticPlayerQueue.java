package com.cometproject.server.game.players.queue;

import com.cometproject.server.tasks.CometThreadManager;


public class StaticPlayerQueue {
    private static PlayerLoginQueueManager mgr;

    static {

    }

    public static void init(CometThreadManager threadManagement) {
        mgr = new PlayerLoginQueueManager(true, threadManagement);
    }

    public static PlayerLoginQueueManager getQueueManager() {
        return mgr;
    }
}
