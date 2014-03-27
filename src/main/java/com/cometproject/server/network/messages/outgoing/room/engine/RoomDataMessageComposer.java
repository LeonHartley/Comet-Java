package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RoomDataMessageComposer {
    public static Composer compose(Room room) {
        return compose(room, false);
    }

    public static Composer compose(Room room, boolean isFollow) {
        Composer msg = new Composer(Composers.RoomDataMessageComposer);
        if(isFollow) {
            msg.writeBoolean(false);
        }

        RoomWriter.writeData(room, msg);

        if(isFollow) {
            msg.writeBoolean(true);
            msg.writeBoolean(false);
            msg.writeBoolean(true);
            msg.writeBoolean(true);
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeBoolean(true);
        }

        return msg;
    }
}
