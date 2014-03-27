package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RoomDataMessageComposer {
    public static Composer compose(Room room) {
        Composer msg = new Composer(Composers.RoomDataMessageComposer);


        RoomWriter.writeData(room, msg);

        return msg;
    }
}
