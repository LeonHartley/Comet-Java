package com.cometproject.server.network.messages.outgoing.group;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Map;

public class GroupBadgesMessageComposer {
    public static Composer compose(Map<Integer, String> badges) {
        Composer msg = new Composer(Composers.GroupBadgesMessageComposer);

        msg.writeInt(badges.size());

        for(Map.Entry<Integer, String> badge : badges.entrySet()) {
            msg.writeInt(badge.getKey());
            msg.writeString(badge.getValue());
        }

        return msg;
    }

    public static Composer compose(int groupId, String badge) {
        Composer msg = new Composer(Composers.GroupBadgesMessageComposer);

        msg.writeInt(1); // Count

        msg.writeInt(groupId);
        msg.writeString(badge);

        return msg;
    }
}
