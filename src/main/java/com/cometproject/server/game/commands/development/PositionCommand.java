package com.cometproject.server.game.commands.development;

import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class PositionCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        sendChat(client.getPlayer().getEntity().getPosition().toString(), client);
    }

    @Override
    public String getPermission() {
        return "debug";
    }

    @Override
    public String getDescription() {
        return "Displays your current position";
    }
}
