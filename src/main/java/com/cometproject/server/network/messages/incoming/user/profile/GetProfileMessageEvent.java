package com.cometproject.server.network.messages.incoming.user.profile;

import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.profile.LoadProfileMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.groups.GroupDao;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.util.List;


public class GetProfileMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();

        PlayerData data = userId == client.getPlayer().getId() ? client.getPlayer().getData() : null;
        PlayerStatistics stats = data != null ? client.getPlayer().getStats() : null;
        List<Integer> groups = data != null ? client.getPlayer().getGroups() : null;

        if (data == null) {
            if (NetworkManager.getInstance().getSessions().getByPlayerId(userId) != null) {
                data = NetworkManager.getInstance().getSessions().getByPlayerId(userId).getPlayer().getData();
                stats = NetworkManager.getInstance().getSessions().getByPlayerId(userId).getPlayer().getStats();
                groups = NetworkManager.getInstance().getSessions().getByPlayerId(userId).getPlayer().getGroups();
            }
        }

        if (data == null) {
            data = PlayerDao.getDataById(userId);
            stats = PlayerDao.getStatisticsById(userId);
            groups = GroupDao.getIdsByPlayerId(userId);
        }

        if (data == null) {
            return;
        }

        client.send(LoadProfileMessageComposer.compose(data, stats, groups, client.getPlayer().getMessenger().getFriendById(userId) != null, false));
    }
}
