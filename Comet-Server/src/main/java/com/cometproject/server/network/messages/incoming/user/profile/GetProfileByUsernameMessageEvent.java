package com.cometproject.server.network.messages.incoming.user.profile;

import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.types.PlayerStatistics;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.profile.LoadProfileMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.storage.api.StorageContext;

import java.util.HashSet;
import java.util.Set;


public class GetProfileByUsernameMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        String username = msg.readString();

        PlayerData data = username.equals(client.getPlayer().getData().getUsername()) ? client.getPlayer().getData() : null;
        PlayerStatistics stats = data != null ? client.getPlayer().getStats() : null;
        final Set<Integer> groups = data != null ? client.getPlayer().getGroups() : new HashSet<>();

        if (data == null) {
            final Session session = NetworkManager.getInstance().getSessions().getByPlayerUsername(username);

            if (session != null && session.getPlayer() != null) {
                data = session.getPlayer().getData();
                stats = session.getPlayer().getStats();

                groups.addAll(session.getPlayer().getGroups());
            }
        }

        if (data == null) {
            int id = PlayerDao.getIdByUsername(username);
            data = PlayerManager.getInstance().getDataByPlayerId(id);
            stats = PlayerDao.getStatisticsById(id);

            StorageContext.getCurrentContext().getGroupRepository().getGroupIdsByPlayerId(id, groups::addAll);
        }

        if (data == null) {
            return;
        }

        client.send(new LoadProfileMessageComposer(data, stats, groups, client.getPlayer().getMessenger().getFriendById(data.getId()) != null, false));

    }
}
