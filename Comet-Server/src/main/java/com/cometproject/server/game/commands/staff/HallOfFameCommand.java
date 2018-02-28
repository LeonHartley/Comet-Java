package com.cometproject.server.game.commands.staff;

import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class HallOfFameCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        final String username = params[0];
        final String badgeCode = params[1];

        if (client.getPlayer() != null) {
            // Check if they already have the badge
        } else {
            // Disconnect
        }
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public String getParameter() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
