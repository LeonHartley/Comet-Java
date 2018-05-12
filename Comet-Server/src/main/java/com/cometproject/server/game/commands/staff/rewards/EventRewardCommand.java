package com.cometproject.server.game.commands.staff.rewards;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.storage.api.StorageContext;
import com.cometproject.storage.api.data.Data;

public class EventRewardCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 2)
            return;

        final String username = params[0];
        final String badge = params[1];

        final Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);
        int playerId = session != null ? session.getPlayer().getId() : PlayerManager.getInstance().getPlayerIdByUsername(username);

        if (playerId == -1) {
            playerId = PlayerDao.getIdByUsername(username);

            if (playerId == 0) {
                sendWhisper(Locale.getOrDefault("command.eventreward.notfound", "Could not find that player!"), client);
                return;
            }
        }

        final Data<Boolean> received = Data.createEmpty();
        StorageContext.getCurrentContext().getRewardRepository().playerReceivedReward(playerId, badge, received::set);

        if (received.has() && received.get()) {
            sendWhisper(Locale.getOrDefault("command.eventreward.already_got", "This player has already received this reward!"), client);
        } else {
            final int points = Integer.parseInt(Locale.getOrDefault("command.eventreward.points", "1"));
            StorageContext.getCurrentContext().getRewardRepository().giveReward(playerId, badge, points);

            if (session != null) {
                session.getPlayer().getData().increasePoints(points);
                session.getPlayer().getInventory().addBadge(badge, false, true);
                session.getPlayer().sendBalance();

                sendWhisper(Locale.getOrDefault("command.eventreward.received", "You just received an event reward!"), session);
            }

            sendWhisper(Locale.getOrDefault("command.eventreward.already_got", "Given event reward successfully"), client);
        }
    }

    @Override
    public String getPermission() {
        return "eventreward_command";
    }

    @Override
    public String getParameter() {
        return "%username% %badge%";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.eventreward.description", "Gives a player a badge & points");
    }
}
