package com.cometproject.server.game.commands.staff.rewards;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;


public class GiveBadgeCommand extends ChatCommand {
    private String logDesc;

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2)
            return;

        final String username = params[0];
        final String badge = params[1];

        Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

        if (session != null) {
            session.getPlayer().getInventory().addBadge(badge, true);
            sendNotif(Locale.get("command.givebadge.success").replace("%username%", username).replace("%badge%", badge), client);
        } else {
            int playerId = PlayerDao.getIdByUsername(username);

            if (playerId == 0) {
                sendNotif(Locale.get("command.givebadge.fail").replace("%username%", username).replace("%badge%", badge), client);
            } else {
                InventoryDao.addBadge(badge, playerId);
                sendNotif(Locale.get("command.givebadge.success").replace("%username%", username).replace("%badge%", badge), client);
            }
        }

        this.logDesc = "El Staff -c le ha dado la placa -d a -e"
                .replace("-c", client.getPlayer().getData().getUsername())
                .replace("-d", badge)
                .replace("-e", username);
    }

    @Override
    public String getPermission() {
        return "givebadge_command";
    }

    @Override
    public String getParameter() {
        return Locale.getOrDefault("command.parameter.username" + " " + "command.parameter.badge", "%username% %badge%");
    }

    @Override
    public String getDescription() {
        return Locale.get("command.givebadge.description");
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
