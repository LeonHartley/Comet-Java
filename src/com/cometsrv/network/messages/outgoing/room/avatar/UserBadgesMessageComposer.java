package com.cometsrv.network.messages.outgoing.room.avatar;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class UserBadgesMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.UserBadgesMessageComposer);

        return msg;
    }
}
