package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class SitCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        //client.getPlayer().getEntity().getPosition()
    }

    @Override
    public String getPermission() {
        return "sit_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.sit.description");
    }
}
