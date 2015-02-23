package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class GiveRoomRighsMessageComposer {
    public static Composer compose(int roomId, int userId, String username) {
        Composer msg = new Composer(Composers.GiveRoomRightsMessageComposer);

        msg.writeInt(roomId);
        msg.writeInt(userId);
        msg.writeString(username);

        return msg;
    }
}
