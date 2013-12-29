package com.cometsrv.game.commands.user;

import com.cometsrv.config.Locale;
import com.cometsrv.game.commands.ChatCommand;
import com.cometsrv.network.sessions.Session;

public class SitCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        // TODO: sit command
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
