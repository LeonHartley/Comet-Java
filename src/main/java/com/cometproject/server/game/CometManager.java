package com.cometproject.server.game;

import com.cometproject.server.tasks.CometThreadManager;
import org.apache.log4j.Logger;


public class CometManager {

    private static GameThread gameThread;

    private static Logger log = Logger.getLogger(CometManager.class.getName());

    public static void init() {


    }

    public static void startCycle() {
        gameThread = new GameThread(CometThreadManager.getInstance());
    }

    public static Logger getLogger() {
        return log;
    }

    public static GameThread getThread() {
        return gameThread;
    }
}
