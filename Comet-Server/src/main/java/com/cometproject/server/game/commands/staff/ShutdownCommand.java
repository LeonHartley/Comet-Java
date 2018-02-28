package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class ShutdownCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        Comet.exit("Server shutdown by command executor: " + client.getPlayer().getData().getUsername() + " / " + client.getPlayer().getData().getId());
    }

    @Override
    public String getPermission() {
        return "shutdown_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.shutdown.description");
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
