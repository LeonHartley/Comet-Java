package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolUserInfoMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class ModToolUserInfoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();

        if (!client.getPlayer().getPermissions().hasPermission("mod_tool")) {
            client.getLogger().error(
                    ModToolUserInfoMessageEvent.class.getName() + " - tried to gather information on user: " + userId);
            client.disconnect();
            return;
        }

        PlayerData playerData = PlayerDao.getDataById(userId);
        PlayerStatistics playerStatistics = PlayerDao.getStatisticsById(userId);

        if (playerData == null || playerStatistics == null) {
            return;
        }

        client.send(ModToolUserInfoMessageComposer.compose(playerData, playerStatistics));
    }
}
