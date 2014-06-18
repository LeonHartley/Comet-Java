package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.sessions.Session;

public class DisconnectCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        String username = params[1];

        Session session = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        if (session == null) {
            return;
        }

        if (session == client) {
            sendChat(Locale.get("command.disconnect.himself"), client);
            return;
        }

        if (session.getPlayer().getPermissions().hasPermission("undisconnectable")) {
            sendChat(Locale.get("command.disconnect.undisconnectable"), client);
            return;
        }

        session.disconnect();
    }

    @Override
    public String getPermission() {
        return "disconnect_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.disconnect.description");
    }
}
