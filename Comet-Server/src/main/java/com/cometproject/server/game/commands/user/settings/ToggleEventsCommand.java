package com.cometproject.server.game.commands.user.settings;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class ToggleEventsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {

    }

    @Override
    public String getPermission() {
        return "toggleevents_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.toggleevents.description");
    }
}
