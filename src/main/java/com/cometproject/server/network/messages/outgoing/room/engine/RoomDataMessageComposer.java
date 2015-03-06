package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class RoomDataMessageComposer extends MessageComposer {
    private final Room room;

    public RoomDataMessageComposer(final Room room) {
        this.room = room;
    }

    @Override
    public short getId() {
        return Composers.RoomDataMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        RoomWriter.entryData(room.getData(), msg);
    }
}
