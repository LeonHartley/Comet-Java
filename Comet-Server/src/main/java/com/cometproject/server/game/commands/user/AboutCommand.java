package com.cometproject.server.game.commands.user;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.stats.CometStats;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameCycle;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.CometRuntime;

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;


public class AboutCommand extends ChatCommand {

    @Override
    public void execute(Session client, String message[]) {
        StringBuilder about = new StringBuilder();
        NumberFormat format = NumberFormat.getInstance();

        CometStats cometStats = Comet.getStats();

        boolean aboutDetailed = client.getPlayer().getPermissions().getRank().aboutDetailed();
        boolean aboutStats = client.getPlayer().getPermissions().getRank().aboutStats();

        if (CometSettings.aboutShowRoomsActive || CometSettings.aboutShowRoomsActive || CometSettings.aboutShowUptime || aboutDetailed) {
            about.append("<b>Server Status</b><br>");

            if (CometSettings.aboutShowPlayersOnline || aboutDetailed)
                about.append("Users online: " + format.format(cometStats.getPlayers()) + "<br>");

            if (CometSettings.aboutShowRoomsActive || aboutDetailed)
                about.append("Active rooms: " + format.format(cometStats.getRooms()) + "<br>");

            if (CometSettings.aboutShowUptime || aboutDetailed)
                about.append("Uptime: " + cometStats.getUptime() + "<br>");

            about.append("Client version: " + Session.CLIENT_VERSION + "<br>");
        }

        // This will be visible to developers on the manager, no need to display it to the end-user.
        if (client.getPlayer().getPermissions().getRank().aboutDetailed()) {
            about.append("<br><b>Server Info</b><br>");
            about.append("Allocated memory: " + format.format(cometStats.getAllocatedMemory()) + "MB<br>");
            about.append("Used memory: " + format.format(cometStats.getUsedMemory()) + "MB<br>");

            about.append("Process ID: " + CometRuntime.processId + "<br>");
            about.append("OS: " + cometStats.getOperatingSystem() + "<br>");
            about.append("CPU cores:  " + cometStats.getCpuCores() + "<br>");
            about.append("Threads:  " + ManagementFactory.getThreadMXBean().getThreadCount() + "<br>");
        }

        if (aboutStats) {
            about.append("<br><br><b>Hotel Stats</b><br>");
            about.append("Online record: " + GameCycle.getInstance().getOnlineRecord() + "<br>");
            about.append("Record since last reboot: " + GameCycle.getInstance().getCurrentOnlineRecord() + "<br>");
        }

        about.append("<br><b>Comet Server (PRIVATE-2020)</b><br>Official Comet Server, a private server made with | in the UK.");

        client.send(new AdvancedAlertMessageComposer(
                "Comet Server - " + Comet.getBuild(),
                about.toString(),
                "www.cometsrv.com", "https://www.cometsrv.com", CometSettings.aboutImg
        ));
    }

    @Override
    public String getPermission() {
        return "about_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.about.description");
    }
}
