package com.cometproject.game.commands.staff;

import com.cometproject.config.Locale;
import com.cometproject.game.commands.ChatCommand;
import com.cometproject.network.sessions.Session;

public class ForceGCCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        System.gc();
        this.sendChat("The garbage collector was successfully forced.", client);
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
