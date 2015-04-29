package com.cometproject.tools;

import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.tools.logger.PacketLogger;
import com.cometproject.tools.packets.PacketManager;
import com.cometproject.tools.ui.CometWindow;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;


public class CometTools {
    private PacketManager packetManager;
    private PacketLogger packetLogger;
    private CometWindow cometWindow;

    public CometTools() {
        Stopwatch stopwatch = Stopwatch.createStarted();

        this.packetLogger = new PacketLogger(this);
        this.cometWindow = new CometWindow(this);

        System.out.println("CometTools was active for: " + (((double) stopwatch.elapsed(TimeUnit.MILLISECONDS)) / 1000) + " seconds");
    }

    private static CometTools instance;

    public static void main(String[] args) {
        instance = new CometTools();
    }

    public static CometTools getInstance() {
        return instance;
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public void setPacketManager(PacketManager packetManager) {
        this.packetManager = packetManager;
    }

    public PacketLogger getPacketLogger() {
        return packetLogger;
    }
}
