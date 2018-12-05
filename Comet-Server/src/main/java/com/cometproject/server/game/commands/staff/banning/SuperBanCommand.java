package com.cometproject.server.game.commands.staff.banning;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.types.BanType;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;


public class SuperBanCommand extends ChatCommand {
    private String logDesc;

    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendNotif(Locale.getOrDefault("command.params.length", "Oops! You did something wrong!"), client);
            return;
        }

        String username = params[0];

        Session user = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (user == null) {
            sendNotif(Locale.getOrDefault("command.user.offline", "This user is offline!"), client);
            return;
        }

        if (user == client || !user.getPlayer().getPermissions().getRank().bannable() || user.getPlayer().getPermissions().getRank().getId() >= client.getPlayer().getPermissions().getRank().getId()) {
            sendNotif(Locale.getOrDefault("command.user.notbannable", "You're not able to ban this user!"), client);
            return;
        }

        client.getPlayer().getStats().addBan();

        user.disconnect("banned");

        int expire = 2147472000; // Approximately latest timestamp

        BanManager.getInstance().banPlayer(BanType.USER, Integer.toString(user.getPlayer().getId()), expire, expire, "Superban", client.getPlayer().getId());

        this.logDesc = "El Staff -c le ha dado SuperBan a -d"
                .replace("-c", client.getPlayer().getData().getUsername())
                .replace("-d", user.getPlayer().getData().getUsername());
    }

    @Override
    public String getPermission() {
        return "superban_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.superban", "%username%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.superban.description");
    }

    @Override
    public boolean bypassFilter() {
        return true;
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
