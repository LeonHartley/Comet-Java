package com.cometsrv.game.commands.staff;

import com.cometsrv.config.Locale;
import com.cometsrv.game.commands.ChatCommand;
import com.cometsrv.network.sessions.Session;

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
