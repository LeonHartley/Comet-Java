package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.types.PlayerSettings;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class ModToolUserCautionMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int playerId = msg.readInt();
        String message = msg.readString();

        if (CometManager.getPlayers().isOnline(playerId)) {
            Session session = Comet.getServer().getNetwork().getSessions().getByPlayerId(playerId);

            if (session != null) {
                session.send(AdvancedAlertMessageComposer.compose(message));
            }
        }

        PlayerStatistics playerStatistics = PlayerDao.getStatisticsById(playerId);

        if (playerStatistics != null) {
            playerStatistics.incrementCautions(1);
        }
    }
}
