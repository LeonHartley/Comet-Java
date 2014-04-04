package com.cometproject.tools;

import com.cometproject.tools.packets.PacketManager;
import com.cometproject.tools.ui.CometWindow;

public class CometTools {
    private PacketManager packetManager;
    private CometWindow cometWindow;

    public CometTools() {
        this.packetManager = new PacketManager();

        this.cometWindow = new CometWindow();
    }

    private static CometTools instance;

    public static void main(String[] args) {
        instance = new CometTools();
    }

    public static CometTools getInstance() {
        return instance;
    }
}
