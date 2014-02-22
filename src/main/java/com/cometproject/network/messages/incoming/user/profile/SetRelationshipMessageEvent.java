package com.cometproject.network.messages.incoming.user.profile;

import com.cometproject.boot.Comet;
import com.cometproject.game.GameEngine;
import com.cometproject.game.players.components.RelationshipComponent;
import com.cometproject.game.players.components.types.RelationshipLevel;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

import java.sql.PreparedStatement;

public class SetRelationshipMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int user = msg.readInt();
        int level = msg.readInt();

        RelationshipComponent relationships = client.getPlayer().getRelationships();

        try {
            if(level == 0) {
                Comet.getServer().getStorage().execute("DELETE FROM player_relationships WHERE player_id = " + client.getPlayer().getId() + " AND partner = " + user);

                if(relationships.getRelationships().containsKey(user)) {
                    relationships.getRelationships().remove(user);
                }
            } else {
                if(relationships.getRelationships().containsKey(user)) {
                    Comet.getServer().getStorage().execute("DELETE FROM player_relationships WHERE player_id = " + client.getPlayer().getId() + " AND partner = " + user);
                    relationships.getRelationships().remove(user);
                }

                String levelString = level == 1 ? "heart" : level == 2 ? "smile" : "bobba";

                PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT into player_relationships (`player_id`, `level`, `partner`) VALUES(?, ?, ?);");

                statement.setInt(1, client.getPlayer().getId());
                statement.setString(2, levelString);
                statement.setInt(3, user);

                statement.execute();

                relationships.getRelationships().put(user, RelationshipLevel.getLevel(levelString));
            }
        } catch(Exception e) {
            GameEngine.getLogger().error("Error while setting relationship", e);
        }
    }
}
