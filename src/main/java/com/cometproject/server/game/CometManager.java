package com.cometproject.server.game;

import com.cometproject.server.tasks.CometThreadManager;
import org.apache.log4j.Logger;


public class CometManager {

    private static Logger log = Logger.getLogger(CometManager.class.getName());

    public static Logger getLogger() {
        return log;
    }
}
