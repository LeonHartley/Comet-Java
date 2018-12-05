package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;


public class KickCommand extends ChatCommand {
    private String logDesc;

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1) {
            sendNotif(Locale.getOrDefault("command.kick.none", "Who do you want to kick?"), client);
            return;
        }

        String username = params[0];
        Session user = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (user == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (username.equals(client.getPlayer().getData().getUsername())) {
            return;
        }

        if (user.getPlayer().getEntity() == null) {
            sendNotif(Locale.getOrDefault("command.user.notinroom", "This user is not in a room."), client);
            return;
        }

        if (!user.getPlayer().getPermissions().getRank().roomKickable()) {
            sendNotif(Locale.getOrDefault("command.kick.unkickable", "You can't kick this player!"), client);
            return;
        }

        user.getPlayer().getEntity().kick();
        isExecuted(client);

        this.logDesc = "El Staff -c ha kickeado al usuario -d"
                .replace("-c", client.getPlayer().getData().getUsername())
                .replace("-d", user.getPlayer().getData().getUsername());
    }

    @Override
    public String getPermission() {
        return "kick_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.kick.description");
    }

    @Override
    public String getLoggableDescription() {
        return this.logDesc;
    }

    @Override
    public boolean isLoggable() {
        return true;
    }
}
