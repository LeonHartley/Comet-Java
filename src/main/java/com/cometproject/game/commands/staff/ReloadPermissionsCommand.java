package com.cometproject.game.commands.staff;

import com.cometproject.config.Locale;
import com.cometproject.game.GameEngine;
import com.cometproject.game.commands.ChatCommand;
import com.cometproject.network.sessions.Session;

public class ReloadPermissionsCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] message) {
        GameEngine.getPermissions().loadPerks();
        GameEngine.getPermissions().loadPermissions();
        GameEngine.getPermissions().loadCommands();

        this.sendChat("Permissions successfully reloaded.", client);
    }

    @Override
    public String getPermission() {
        return "reload_permissions_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.reload_permissions.description");
    }
}
