package com.cometproject.network.messages.outgoing.room.avatar;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class UserBadgesMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.UserBadgesMessageComposer);

        return msg;
    }
}
