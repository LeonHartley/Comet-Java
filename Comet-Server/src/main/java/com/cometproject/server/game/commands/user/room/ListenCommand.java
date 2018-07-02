package com.cometproject.server.game.commands.user.room;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;

public class ListenCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.get("command.listen.notfound"), client);
            return;
        }

        final String username = params[0];
        final Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (session == null || session.getPlayer() == null ||
                session.getPlayer().getData().getRank() > client.getPlayer().getData().getRank()) {
            sendNotif(Locale.get("command.listen.notfound"), client);
            return;
        }

        if(session.getPlayer().getListeningPlayers().contains(client.getPlayer().getId())) {
            session.getPlayer().getListeningPlayers().remove(client.getPlayer().getId());
        } else {
            session.getPlayer().getListeningPlayers().add(client.getPlayer().getId());
            sendNotif(Locale.get("command.listen.listening").replace("%username%", session.getPlayer().getData().getUsername()), client);
        }
    }

    @Override
    public String getPermission() {
        return "listen_command";
    }

    @Override
    public String getParameter() {
        return "%user%";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.listen.description");
    }
}
