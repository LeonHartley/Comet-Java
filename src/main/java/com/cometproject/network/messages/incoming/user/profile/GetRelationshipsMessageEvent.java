package com.cometproject.network.messages.incoming.user.profile;

import com.cometproject.boot.Comet;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.user.profile.RelationshipsMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class GetRelationshipsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();

        if(userId == client.getPlayer().getId()) {
            client.send(RelationshipsMessageComposer.compose(client.getPlayer().getRelationships()));
            return;
        }

        if(Comet.getServer().getNetwork().getSessions().getByPlayerId(userId) != null) {
            client.send(RelationshipsMessageComposer.compose(Comet.getServer().getNetwork().getSessions().getByPlayerId(userId).getPlayer().getRelationships()));
            return;
        }

        // TODO: show relationships for offline users!!!
        client.send(RelationshipsMessageComposer.compose(userId));
    }
}
