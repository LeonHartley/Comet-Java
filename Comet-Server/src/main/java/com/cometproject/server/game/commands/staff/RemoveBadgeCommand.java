package com.cometproject.server.game.commands.staff;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;


public class RemoveBadgeCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2)
            return;

        final String username = params[0];
        final String badge = params[1];

        Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(params[0]);

        if (session != null) {
            session.getPlayer().getInventory().removeBadge(params[1], true);
            sendNotif(Locale.get("command.removebadge.success").replace("%username%", username).replace("%badge%", badge), client);
        } else {
            sendNotif(Locale.getOrDefault("command.removebadge.userisoff", "This user is not online, you can only remove badges from online users"), client);
        }
    }

    @Override
    public String getPermission() {
        return "removebadge_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username" + " " + "command.parameter.badge", "%username% %badge%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.removebadge.description");
    }
}
