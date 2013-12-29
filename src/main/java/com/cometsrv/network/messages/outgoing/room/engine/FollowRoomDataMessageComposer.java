package com.cometsrv.network.messages.outgoing.room.engine;

import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.RoomWriter;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class FollowRoomDataMessageComposer {
    public static Composer compose(Room room) {
        Composer msg = new Composer(Composers.RoomDataMessageComposer);

        msg.writeBoolean(false);

        RoomWriter.writeInfo(room, msg);

        msg.writeBoolean(true);
        msg.writeBoolean(false);
        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeBoolean(true);

        return msg;
    }
}
