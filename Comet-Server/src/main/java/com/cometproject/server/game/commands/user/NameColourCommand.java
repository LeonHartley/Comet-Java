package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class NameColourCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            return;
        }

        final String colour = params[0];

        client.getPlayer().getData().setNameColour(colour);
        client.getPlayer().getData().save();
    }

    @Override
    public String getPermission() {
        return "namecolour_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.namecolour.param", "%colour%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.namecolour.description");
    }
}
