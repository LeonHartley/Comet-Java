package com.cometsrv.network.messages.outgoing.user.inventory;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;
import javolution.util.FastMap;

import java.util.Map;

public class BadgeInventoryMessageComposer {
    public static Composer compose(Map<String, Integer> badges) {
        Composer msg = new Composer(Composers.BadgeInventoryMessageComposer);

        Map<String, Integer> activeBadges = new FastMap<>();

        msg.writeInt(badges.size());

        for(Map.Entry<String, Integer> badge : badges.entrySet()) {
            if(badge.getValue() > 0) {
                activeBadges.put(badge.getKey(), badge.getValue());
            }

            msg.writeInt(0);
            msg.writeString(badge.getKey());
        }

        msg.writeInt(activeBadges.size());

        for(Map.Entry<String, Integer> badge : activeBadges.entrySet()) {
            msg.writeInt(badge.getValue());
            msg.writeString(badge.getKey());
        }

        activeBadges.clear();
        return msg;
    }
}
