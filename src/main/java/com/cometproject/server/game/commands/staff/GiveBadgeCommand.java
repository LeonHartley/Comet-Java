package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;


public class GiveBadgeCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        // TODO: WHAT THE FUCK IS THIS?!
        client.getPlayer().getInventory().addBadge(params[1], true);
    }

    @Override
    public String getPermission() {
        return "givebadge_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.givebadge.description");
    }
}
