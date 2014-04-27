package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class ReloadCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        if(params.length < 1)
            return;

        String command = params[0];

        switch(command) {
            case "bans":

                break;

            case "catalog":

                break;

            case "navigator":

                break;

            case "permissions":

                break;

            case "config":

                break;

        }
    }

    @Override
    public String getPermission() {
        return "reload_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.reload.description");
    }
}
