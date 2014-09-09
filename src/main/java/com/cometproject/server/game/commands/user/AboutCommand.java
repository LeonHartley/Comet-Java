package com.cometproject.server.game.commands.user;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.CometRuntime;
import com.cometproject.server.utilities.CometStats;
import com.cometproject.server.utilities.TimeSpan;

import java.text.NumberFormat;

public class AboutCommand extends ChatCommand {

    @Override
    public void execute(Session client, String message[]) {
        StringBuilder about = new StringBuilder();
        NumberFormat format = NumberFormat.getInstance();

        CometStats cometStats = CometStats.get();

        about.append("Comet Server is a unique Habbo emulator written in Java.<br><br>");

        if (CometSettings.showActiveRoomsInAbout || CometSettings.showActiveRoomsInAbout || CometSettings.showUptimeInAbout || client.getPlayer().getPermissions().hasPermission("about_detailed")) {
            about.append("<b>Server Status</b><br>");

            if (CometSettings.showUsersOnlineInAbout || client.getPlayer().getPermissions().hasPermission("about_detailed"))
                about.append("Users online: " + format.format(cometStats.getPlayers()) + "<br>");

            if (CometSettings.showActiveRoomsInAbout || client.getPlayer().getPermissions().hasPermission("about_detailed"))
                about.append("Active rooms: " + format.format(cometStats.getRooms()) + "<br>");

            if (CometSettings.showUptimeInAbout || client.getPlayer().getPermissions().hasPermission("about_detailed"))
                about.append("Uptime: " + cometStats.getUptime() + "<br>");
        }

        if (client.getPlayer().getPermissions().hasPermission("about_detailed")) {
            about.append("<br><b>Server Info</b><br>");
            about.append("Process ID: " + CometRuntime.processId + "<br>");
            about.append("Allocated memory: " + format.format(cometStats.getAllocatedMemory()) + "MB<br>");
            about.append("Used memory: " + format.format(cometStats.getUsedMemory()) + "MB<br>");
            about.append("OS: " + cometStats.getOperatingSystem() + "<br>");
            about.append("CPU cores:  " + cometStats.getCpuCores() + "<br>");

            about.append("<br><br><b>Hotel Stats</b><br>");
            about.append("Current online record: " + CometManager.getThread().getOnlineRecord());
        }

        client.send(AdvancedAlertMessageComposer.compose(
                "Comet Server - " + Comet.getBuild(),
                about.toString(),
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
