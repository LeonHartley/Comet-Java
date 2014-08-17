package com.cometproject.server.game.commands.vip;

import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class ConvertCreditsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {

    }

    @Override
    public String getPermission() {
        return "convertcredits_command";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
