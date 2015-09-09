package com.cometproject.server.network.messages.incoming.user.profile;

import com.cometproject.server.game.players.components.RelationshipComponent;
import com.cometproject.server.game.players.components.types.messenger.RelationshipLevel;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.relationships.RelationshipDao;


public class SetRelationshipMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int user = msg.readInt();
        int level = msg.readInt();

        if(client.getPlayer().getMessenger().getFriendById(user) == null) {
            return;
        }

        RelationshipComponent relationships = client.getPlayer().getRelationships();

        if(relationships.getRelationships().size() >= 100) {
            // TODO: Allow this to be configured.
            return;
        }

        if (level == 0) {
            RelationshipDao.deleteRelationship(client.getPlayer().getId(), user);

            if (relationships.getRelationships().containsKey(user)) {
                relationships.getRelationships().remove(user);
            }
        } else {
            String levelString = level == 1 ? "HEART" : level == 2 ? "SMILE" : "BOBBA";

            if (relationships.getRelationships().containsKey(user)) {

                RelationshipDao.updateRelationship(client.getPlayer().getId(), user, levelString);
                relationships.getRelationships().replace(user, RelationshipLevel.valueOf(levelString));
                return;
            }

            RelationshipDao.createRelationship(client.getPlayer().getId(), user, levelString);
            relationships.getRelationships().put(user, RelationshipLevel.valueOf(levelString));
        }
    }
}
