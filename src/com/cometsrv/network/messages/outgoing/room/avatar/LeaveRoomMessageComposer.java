package com.cometsrv.network.messages.outgoing.room.avatar;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class LeaveRoomMessageComposer {
    public static Composer compose(int userId) {
        Composer msg = new Composer(Composers.LeaveRoomMessageComposer);
        msg.writeString(userId);
        return msg;
    }
}
