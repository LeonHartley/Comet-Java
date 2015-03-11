package com.cometproject.server.game.commands.user.group;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class DeleteGroupCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {

    }

    @Override
    public String getPermission() {
        return "deletegroup_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.deletegroup.description");
    }
}
