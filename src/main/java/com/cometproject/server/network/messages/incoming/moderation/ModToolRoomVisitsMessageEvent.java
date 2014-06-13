package com.cometproject.server.network.messages.incoming.moderation;

import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolRoomVisitsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

public class ModToolRoomVisitsMessageEvent implements IEvent {

    @Override
    public void handle(Session client, Event msg) throws Exception {
        int playerId = msg.readInt();

        client.send(ModToolRoomVisitsMessageComposer.compose(playerId, PlayerDao.getUsernameByPlayerId(playerId), LogQueries.getLastRoomVisits(playerId, 100)));
    }
}
