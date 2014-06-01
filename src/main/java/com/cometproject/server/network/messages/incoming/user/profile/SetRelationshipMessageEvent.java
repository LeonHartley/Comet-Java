package com.cometproject.server.network.messages.incoming.user.profile;

import com.cometproject.server.game.players.components.RelationshipComponent;
import com.cometproject.server.game.players.components.types.RelationshipLevel;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.relationships.RelationshipDao;

public class SetRelationshipMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int user = msg.readInt();
        int level = msg.readInt();

        RelationshipComponent relationships = client.getPlayer().getRelationships();

        if (level == 0) {
            RelationshipDao.deleteRelationship(client.getPlayer().getId(), user);

            if (relationships.getRelationships().containsKey(user)) {
                relationships.getRelationships().remove(user);
            }
        } else {
            // TODO: should probs update and not delete ;-)
            if (relationships.getRelationships().containsKey(user)) {
                RelationshipDao.deleteRelationship(client.getPlayer().getId(), user);
                relationships.getRelationships().remove(user);
            }

            String levelString = level == 1 ? "heart" : level == 2 ? "smile" : "bobba";

            RelationshipDao.createRelationship(client.getPlayer().getId(), user, levelString);
            relationships.getRelationships().put(user, RelationshipLevel.getLevel(levelString));
        }
    }
}
