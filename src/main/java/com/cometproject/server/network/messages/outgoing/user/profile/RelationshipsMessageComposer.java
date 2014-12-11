package com.cometproject.server.network.messages.outgoing.user.profile;

import com.cometproject.server.game.players.components.RelationshipComponent;
import com.cometproject.server.game.players.components.types.RelationshipLevel;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Map;


public class RelationshipsMessageComposer {
    public static Composer compose(int playerId, Map<Integer, RelationshipLevel> relationships) {
        Composer msg = new Composer(Composers.RelationshipMessageComposer);

        msg.writeInt(playerId);

        if (relationships.size() == 0) {
            msg.writeInt(0);
            return msg;
        }

        msg.writeInt(relationships.size());

        int hearts = RelationshipComponent.countByLevel(RelationshipLevel.HEART, relationships);
        int smiles = RelationshipComponent.countByLevel(RelationshipLevel.SMILE, relationships);
        int bobbas = RelationshipComponent.countByLevel(RelationshipLevel.BOBBA, relationships);

        List<Integer> relationshipKeys = Lists.newArrayList(relationships.keySet());
        Collections.shuffle(relationshipKeys);

        for (Integer relationshipKey : relationshipKeys) {
            RelationshipLevel level = relationships.get(relationshipKey);

            PlayerData data = PlayerDao.getDataById(relationshipKey);

            if (data == null) {
                msg.writeInt(0);
                msg.writeInt(0);
                msg.writeInt(0); // id
                msg.writeString("Username");
                msg.writeString("hr-115-42.hd-190-1.ch-215-62.lg-285-91.sh-290-62");
            } else {
                msg.writeInt(level.getLevelId());
                msg.writeInt(level == RelationshipLevel.HEART ? hearts : level == RelationshipLevel.SMILE ? smiles : bobbas);
                msg.writeInt(data.getId());
                msg.writeString(data.getUsername());
                msg.writeString(data.getFigure());
            }

        }

        return msg;
    }

    public static Composer compose(int id) {
        Composer msg = new Composer(Composers.RelationshipMessageComposer);

        msg.writeInt(id);
        msg.writeInt(0);

        return msg;
    }
}
