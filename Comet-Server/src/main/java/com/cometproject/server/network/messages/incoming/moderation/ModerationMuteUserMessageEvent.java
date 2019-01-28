package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.ws.messages.alerts.MutedMessage;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class ModerationMuteUserMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int playerId = msg.readInt();
        final int muteLength = msg.readInt();

        // TODO: use length.

        if (!client.getPlayer().getPermissions().getRank().modTool()) {
            client.disconnect();

            return;
        }

        final int timeMuted = (int) Comet.getTime() + muteLength * 60;
        final Session session = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

        if (session != null) {
            session.getPlayer().getData().setTimeMuted(timeMuted);
            if (session.getWsChannel() != null) {
                session.sendWs(new MutedMessage(MutedMessage.MuteType.MODERATOR_MUTE, true, null));
            } else {
                session.send(new AdvancedAlertMessageComposer(Locale.get("command.mute.muted")));
            }
        }

        PlayerDao.addTimeMute(playerId, timeMuted);
    }
}
