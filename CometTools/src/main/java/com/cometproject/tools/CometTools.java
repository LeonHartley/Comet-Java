package com.cometproject.tools;

import com.cometproject.tools.packets.PacketManager;
import com.cometproject.tools.ui.CometWindow;

public class CometTools {
    private PacketManager packetManager;
    private CometWindow cometWindow;

    public CometTools() {
        this.cometWindow = new CometWindow();
        this.packetManager = new PacketManager();
    }

    private static CometTools instance;

    public static void main(String[] args) {
        instance = new CometTools();
    }

    public static CometTools getInstance() {
        return instance;
    }
}
