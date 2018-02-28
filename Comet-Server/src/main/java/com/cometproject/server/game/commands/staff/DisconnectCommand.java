package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;


public class DisconnectCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.disconnect.none", "Who do you want to disconnect?"), client);
            return;
        }

        String username = params[0];

        Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (session == null) {
            sendNotif(Locale.get("command.user.offline"), client);
            return;
        }

        if (session == client) {
            sendNotif(Locale.get("command.disconnect.himself"), client);
            return;
        }

        if (!session.getPlayer().getPermissions().getRank().disconnectable()) {
            sendNotif(Locale.get("command.disconnect.undisconnectable"), client);
            return;
        }

        session.disconnect();
        isExecuted(client);
    }

    @Override
    public String getPermission() {
        return "disconnect_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.disconnect.description");
    }

    @Override
    public boolean bypassFilter() {
        return true;
    }
}
