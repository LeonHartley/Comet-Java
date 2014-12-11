package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class LeaveRoomMessageComposer {
    public static Composer compose(int userId) {
        Composer msg = new Composer(Composers.UserLeftRoomMessageComposer);
        msg.writeString(userId);
        return msg;
    }
}
