package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class ModToolUserCautionMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int playerId = msg.readInt();
        String message = msg.readString();

        if (!client.getPlayer().getPermissions().getRank().modTool()) {
            // fuck off
            client.getLogger().error(
                    ModToolUserCautionMessageEvent.class.getName() + " - tried to caution user with ID: " + playerId + " and message: " + message);
            client.disconnect();
            return;
        }

        if (PlayerManager.getInstance().isOnline(playerId)) {
            Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

            if (session != null) {
                session.send(new AdvancedAlertMessageComposer(message));
            }
        }

        PlayerStatistics playerStatistics = PlayerDao.getStatisticsById(playerId);

        if (playerStatistics != null) {
            playerStatistics.incrementCautions(1);
        }
    }
}
