package com.cometproject.server.network.messages.incoming.user.profile;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.profile.RelationshipsMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.relationships.RelationshipDao;

public class GetRelationshipsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();

        if (userId == client.getPlayer().getId()) {
            client.send(RelationshipsMessageComposer.compose(client.getPlayer().getId(), client.getPlayer().getRelationships().getRelationships()));
            return;
        }

        if (Comet.getServer().getNetwork().getSessions().getByPlayerId(userId) != null) {
            client.send(RelationshipsMessageComposer.compose(userId, Comet.getServer().getNetwork().getSessions().getByPlayerId(userId).getPlayer().getRelationships().getRelationships()));
            return;
        }

        client.send(RelationshipsMessageComposer.compose(userId, RelationshipDao.getRelationshipsByPlayerId(userId)));
    }
}
