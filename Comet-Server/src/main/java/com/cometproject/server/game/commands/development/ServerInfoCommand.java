package com.cometproject.server.game.commands.development;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.clients.ClientHandler;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.sessions.Session;

public class ServerInfoCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        client.send(new AlertMessageComposer("This hotel is powered by Comet Server (" + Comet.getBuild() + ")" +
                "<br><br><b>Statistics</b><br>Active Connections: " + ClientHandler.getInstance().getActiveConnections() + "<br>Players online: " + PlayerManager.getInstance().size()));
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
