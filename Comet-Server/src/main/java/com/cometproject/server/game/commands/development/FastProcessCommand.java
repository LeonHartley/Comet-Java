package com.cometproject.server.game.commands.development;

import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class FastProcessCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        client.getPlayer().getEntity().getRoom().getProcess().setDelay(490);
    }

    @Override
    public String getPermission() {
        return "dev";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
