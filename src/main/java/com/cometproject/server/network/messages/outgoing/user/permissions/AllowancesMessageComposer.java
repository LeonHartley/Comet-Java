package com.cometproject.server.network.messages.outgoing.user.permissions;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.permissions.types.Perk;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Map;

public class AllowancesMessageComposer {
    public static Composer compose(int rank) {
        Composer msg = new Composer(Composers.AllowancesMessageComposer);

        msg.writeInt(GameEngine.getPermissions().getPerks().size());

        for(Map.Entry<Integer, Perk> perk : GameEngine.getPermissions().getPerks().entrySet()) {
            msg.writeString(perk.getValue().getTitle());
            msg.writeString(perk.getValue().getData());

            if(perk.getValue().doesOverride()) {
                msg.writeBoolean(perk.getValue().getDefault());
            } else {
                msg.writeBoolean(perk.getValue().getRank() <= rank);
            }

        }


        return msg;
    }
}
