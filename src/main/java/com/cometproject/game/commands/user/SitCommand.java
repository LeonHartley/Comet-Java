package com.cometproject.game.commands.user;

import com.cometproject.config.Locale;
import com.cometproject.game.commands.ChatCommand;
import com.cometproject.network.sessions.Session;

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
