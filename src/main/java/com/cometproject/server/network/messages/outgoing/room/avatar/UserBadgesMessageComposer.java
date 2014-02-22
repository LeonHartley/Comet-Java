package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class UserBadgesMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.UserBadgesMessageComposer);

        return msg;
    }
}
