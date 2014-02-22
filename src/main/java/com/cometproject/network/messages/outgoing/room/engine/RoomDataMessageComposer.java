package com.cometproject.network.messages.outgoing.room.engine;

import com.cometproject.game.rooms.types.Room;
import com.cometproject.game.rooms.types.RoomWriter;
import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class RoomDataMessageComposer {
    public static Composer compose(Room room) {
        Composer msg = new Composer(Composers.RoomDataMessageComposer);

        RoomWriter.writeData(room, msg);

        return msg;
    }
}
