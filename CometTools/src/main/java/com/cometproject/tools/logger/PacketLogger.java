package com.cometproject.tools.logger;

import com.cometproject.tools.CometTools;

public class PacketLogger {
    private CometTools tools;
    private boolean isActive = false;

    public PacketLogger(CometTools tools) {
        this.isActive = false;
    }

    public boolean start() {
        try {

            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public boolean stop() {
        try {
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public boolean getIsActive() {
        return this.isActive;
    }
}
