package com.cometproject.server.game.commands.user.room;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class HeightCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        double height;

        try {
            height = Double.parseDouble(params[0]);
        } catch (Exception e) {
            height = -1;
        }

        if (height < -1 || height > 64) {
            sendNotif(Locale.get("command.height.invalid"), client);
            return;
        }

        client.getPlayer().setItemPlacementHeight(height);
        sendNotif(Locale.get("command.height.set").replace("%height%", "" + height), client);
    }

    @Override
    public String getPermission() {
        return "height_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.height.param", "%height%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.height.description");
    }
}

