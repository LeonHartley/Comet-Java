package com.cometsrv.network.messages.outgoing.user.profile;

import com.cometsrv.game.players.components.RelationshipComponent;
import com.cometsrv.game.players.components.types.RelationshipLevel;
import com.cometsrv.game.players.data.PlayerData;
import com.cometsrv.game.players.data.PlayerLoader;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

import java.util.Map;

public class RelationshipsMessageComposer {
    public static Composer compose(RelationshipComponent relationships) {
        Composer msg = new Composer(Composers.RelationshipsMessageComposer);

        msg.writeInt(relationships.getPlayer().getId());

        if(relationships.count() == 0) {
            msg.writeInt(0);
            return msg;
        }

        msg.writeInt(relationships.count());

        int hearts = relationships.countByLevel(RelationshipLevel.HEART);
        int smiles = relationships.countByLevel(RelationshipLevel.SMILE);
        int bobbas  = relationships.countByLevel(RelationshipLevel.BOBBA);

        for(Map.Entry<Integer, RelationshipLevel> rel : relationships.getRelationships().entrySet()) {
            PlayerData data = PlayerLoader.loadDataById(rel.getKey());

            if(data == null) {
                msg.writeInt(0);
                msg.writeInt(0);
                msg.writeInt(0); // id
                msg.writeString("Placeholder");
               // msg.writeString("hr-115-42.hd-190-1.ch-215-62.lg-285-91.sh-290-62"); // newer versions only apparently.
            } else {
                msg.writeInt(RelationshipLevel.getInt(rel.getValue()));
                msg.writeInt(rel.getValue() == RelationshipLevel.HEART ? hearts : rel.getValue() == RelationshipLevel.SMILE ? smiles : bobbas);
                msg.writeInt(data.getId());
                msg.writeString(data.getUsername());
                //msg.writeString(data.getFigure()); // newer versions only apparently.
            }
        }

        return msg;
    }

    public static Composer compose(int id) {
        Composer msg = new Composer(Composers.RelationshipsMessageComposer);

        msg.writeInt(id);
        msg.writeInt(0);

        return msg;
    }
}
