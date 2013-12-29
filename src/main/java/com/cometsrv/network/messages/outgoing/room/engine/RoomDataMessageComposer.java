package com.cometsrv.network.messages.outgoing.room.engine;

import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.rooms.types.RoomWriter;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class RoomDataMessageComposer {
    public static Composer compose(Room room) {
        Composer msg = new Composer(Composers.RoomDataMessageComposer);

        RoomWriter.writeData(room, msg);

        return msg;
    }
}
