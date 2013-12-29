package com.cometsrv.network.http.system;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.GameEngine;
import com.cometsrv.network.http.ManagementPage;
import com.cometsrv.utilities.TimeSpan;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

public class StatsPage extends ManagementPage{
    @Override
    public boolean handle(HttpExchange e, String uri) {
        Runtime runtime = Runtime.getRuntime();

        ServerStatus status = new ServerStatus(
                Comet.getServer().getNetwork().getSessions().getUsersOnlineCount(),
                GameEngine.getRooms().getActiveRooms().size(),
                TimeSpan.millisecondsToDate(System.currentTimeMillis() - Comet.start),
                ((runtime.totalMemory() / 1024) / 1024),
                ((runtime.totalMemory() / 1024) / 1024) - ((runtime.freeMemory() / 1024) / 1024),
                System.getProperty("os.name")+ " (" + System.getProperty("os.arch")+ ")",
                runtime.availableProcessors(),
                Comet.getServer().getStorage().getConnectionCount(),
                Comet.getServer().getSystemMonitor().getCycleCount()
        );

        Comet.getServer().getNetwork().getManagement().sendResponse(new Gson().toJson(status), e);
        return true;
    }
}
