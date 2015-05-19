package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.RoomWriter;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class FollowRoomDataMessageComposer extends MessageComposer {
    private final RoomData roomData;

    public FollowRoomDataMessageComposer(final RoomData room) {
        this.roomData = room;
    }

    @Override
    public short getId() {
        return Composers.RoomDataMessageComposer;
    }

    @Override
    public void compose(IComposer composer) {
        RoomWriter.entryData(this.roomData, composer, false, false);
    }
}
