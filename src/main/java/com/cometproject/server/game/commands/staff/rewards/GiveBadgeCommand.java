package com.cometproject.server.game.commands.staff.rewards;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;


public class GiveBadgeCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2)
            return;

        String username = params[0];

        Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (session != null)
            session.getPlayer().getInventory().addBadge(params[1], true);
    }

    @Override
    public String getPermission() {
        return "givebadge_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.givebadge.description");
    }
}
