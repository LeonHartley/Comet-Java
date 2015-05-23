package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;


public class RoomDataMessageComposer extends MessageComposer {
    private final Room room;
    private final boolean checkEntry;

    public RoomDataMessageComposer(final Room room, boolean checkEntry) {
        this.room = room;
        this.checkEntry = checkEntry;
    }

    public RoomDataMessageComposer(final Room room) {
        this.room = room;
        this.checkEntry = true;
    }

    @Override
    public short getId() {
        return Composers.RoomDataMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        RoomWriter.entryData(room.getData(), msg, true, this.checkEntry);
    }
}
