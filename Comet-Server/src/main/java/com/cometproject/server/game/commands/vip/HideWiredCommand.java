package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class HideWiredCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {

    }

    @Override
    public String getPermission() {
        return "hidewired_command";
    }

    @Override
    public String getParameter() {
        return null;
    }

    @Override
    public String getDescription() {
        return Locale.get("command.hidewired.description");
    }
}
