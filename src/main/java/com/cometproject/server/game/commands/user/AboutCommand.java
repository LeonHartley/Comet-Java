package com.cometproject.server.game.commands.user;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.CometRuntime;
import com.cometproject.server.utilities.TimeSpan;

import java.text.NumberFormat;

public class AboutCommand extends ChatCommand {

    @Override
    public void execute(Session client, String message[]) {
        StringBuilder about = new StringBuilder();
        Runtime runtime = Runtime.getRuntime();
        NumberFormat format = NumberFormat.getInstance();

        about.append("Comet Server is a unique Habbo emulator written in Java.<br><br>");

        if (CometSettings.showActiveRoomsInAbout || CometSettings.showActiveRoomsInAbout || CometSettings.showUptimeInAbout || client.getPlayer().getPermissions().hasPermission("about_detailed")) {
            about.append("<b>Server Status</b><br>");

            if (CometSettings.showUsersOnlineInAbout || client.getPlayer().getPermissions().hasPermission("about_detailed"))
                about.append("Users online: " + format.format(Comet.getServer().getNetwork().getSessions().getUsersOnlineCount()) + "<br>");

            if (CometSettings.showActiveRoomsInAbout || client.getPlayer().getPermissions().hasPermission("about_detailed"))
                about.append("Active rooms: " + format.format(CometManager.getRooms().getRoomInstances().size()) + "<br>");

            if (CometSettings.showUptimeInAbout || client.getPlayer().getPermissions().hasPermission("about_detailed"))
                about.append("Uptime: " + TimeSpan.millisecondsToDate(System.currentTimeMillis() - Comet.start) + "<br>");
        }

        if (client.getPlayer().getPermissions().hasPermission("about_detailed")) {
            about.append("<br><b>Server Info</b><br>");
            about.append("Process ID: " + CometRuntime.processId + "<br>");
            about.append("Allocated memory: " + format.format(((runtime.totalMemory() / 1024) / 1024)) + "MB<br>");
            about.append("Used memory: " + format.format(((runtime.totalMemory() / 1024) / 1024) - ((runtime.freeMemory() / 1024) / 1024)) + "MB<br>");
            about.append("OS: " + CometRuntime.operatingSystem + " (" + CometRuntime.operatingSystemArchitecture + ")<br>");
            about.append("CPU cores:  " + runtime.availableProcessors() + "<br>");

            about.append("<br><br><b>Hotel Stats</b><br>");
            about.append("Current online record: " + CometManager.getThread().getOnlineRecord());
        }

        if (message.length != 0) {
            String param = message[0];

            about.append("<br><br>");

            switch (param) {
                case "room": {
                    if (client.getPlayer().getEntity() != null) {
                        Room room = client.getPlayer().getEntity().getRoom();

                        about.append("<b>Room Info</b><br>");
                        about.append("Loaded time: " + TimeSpan.millisecondsToDate(System.currentTimeMillis() - (long) room.getAttribute("loadTime")));
                    }

                    break;
                }

                case "rooms":
                    about.append("<b>LRU Stats</b><br>");

                    about.append("Current size: " + CometManager.getRooms().getRoomDataInstances().getStats().getCurrentSize() + "<br>");
                    about.append("Cumulative evictions: " + CometManager.getRooms().getRoomDataInstances().getStats().getCumulativeEvictions() + "<br>");
                    about.append("Cumulative hits: " + CometManager.getRooms().getRoomDataInstances().getStats().getCumulativeHits() + "<br>");
                    about.append("Cumulative look-ups: " + CometManager.getRooms().getRoomDataInstances().getStats().getCumulativeLookups() + "<br>");
                    about.append("Cumulative misses: " + CometManager.getRooms().getRoomDataInstances().getStats().getCumulativeMisses() + "<br>");
                    about.append("Cumulative non-live-puts: " + CometManager.getRooms().getRoomDataInstances().getStats().getCumulativeNonLivePuts() + "<br>");
                    about.append("Cumulative puts: " + CometManager.getRooms().getRoomDataInstances().getStats().getCumulativePuts() + "<br><br>");

                    about.append("<b>Room Manager Info</b><br>");
                    about.append("Room data count: " + CometManager.getRooms().getRoomDataInstances().size() + "<br>");
                    about.append("Room instance count: " + CometManager.getRooms().getRoomInstances().size() + "<br>");
                    break;
            }
        }

        client.send(AdvancedAlertMessageComposer.compose(
                "Comet Server - " + Comet.getBuild(),
                about.toString(),
                "Learn more about Comet Server",
                "http://cometproject.com/?ref=" + CometSettings.hotelName,
                CometSettings.aboutImg
        ));
    }

    @Override
    public String getPermission() {
        return "about_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.about.description");
    }
}
