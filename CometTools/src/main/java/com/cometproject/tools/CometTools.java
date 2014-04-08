package com.cometproject.tools;

import com.cometproject.tools.packets.PacketManager;
import com.cometproject.tools.ui.CometWindow;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

public class CometTools {
    private PacketManager packetManager;
    private CometWindow cometWindow;

    public CometTools() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        this.packetManager = new PacketManager();
        //this.cometWindow = new CometWindow();

        System.out.println("CometTools was active for: " + (((double) stopwatch.elapsed(TimeUnit.MILLISECONDS)) / 1000) + " seconds");
    }

    private static CometTools instance;

    public static void main(String[] args) {
        instance = new CometTools();
    }

    public static CometTools getInstance() {
        return instance;
    }
}
