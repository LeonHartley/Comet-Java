package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class ForceGCCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        System.gc();
        this.sendChat(Locale.get("command.force_gc.successful"), client);
    }

    @Override
    public String getPermission() {
        return "forcegc_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.force_gc.description");
    }
}
