package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RoomPlayerUnbannedMessageComposer {
    public static Composer compose(int roomId, int playerId) {
        Composer msg = new Composer(Composers.RoomPlayerUnbannedMessageComposer);

        msg.writeInt(roomId);
        msg.writeInt(playerId);

        return msg;
    }
}
