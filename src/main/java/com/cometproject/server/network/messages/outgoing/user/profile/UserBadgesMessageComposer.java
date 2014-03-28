package com.cometproject.server.network.messages.outgoing.user.profile;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Map;

public class UserBadgesMessageComposer {
    public static Composer compose(int userId, Map<String, Integer> badges) {
        Composer msg = new Composer(Composers.UserBadgesMessageComposer);

        msg.writeInt(userId);
        msg.writeInt(badges.size());

        for(Map.Entry<String, Integer> badge : badges.entrySet()) {
            if(badge.getValue() > 0) {
                msg.writeInt(badge.getValue());
                msg.writeString(badge.getKey());
            }
        }

        return msg;
    }
}
