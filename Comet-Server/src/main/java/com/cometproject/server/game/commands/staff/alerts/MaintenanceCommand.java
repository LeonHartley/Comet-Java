package com.cometproject.server.game.commands.staff.alerts;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.notification.HotelMaintenanceMessageComposer;
import com.cometproject.server.network.sessions.Session;

import java.util.Calendar;


public class MaintenanceCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        int hour;
        int minute;

        if (params.length == 2) {
            hour = Integer.parseInt(params[0]);
            minute = Integer.parseInt(params[1]);

            if (hour > 23 || hour < 0)
                hour = 0;

            if (minute > 59 || minute < 0)
                minute = 0;
        } else {
            Calendar calendar = Calendar.getInstance();

            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE) + 10;
        }

        NetworkManager.getInstance().getSessions().broadcast(new HotelMaintenanceMessageComposer(hour, minute, false));
    }

    @Override
    public String getPermission() {
        return "maintenance_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.maintenance.description");
    }
}
