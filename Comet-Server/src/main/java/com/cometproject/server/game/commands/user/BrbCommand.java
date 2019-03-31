package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class BrbCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        client.getPlayer().getEntity().setAway();

        sendWhisper("You're now away!", client);
    }

    @Override
    public String getPermission() {
        return "away_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.brb.description");
    }
}
