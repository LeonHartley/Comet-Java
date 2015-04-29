package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;


public class RoomPlayerUnbannedMessageComposer extends MessageComposer {
    private final int roomId;
    private final int playerId;

    public RoomPlayerUnbannedMessageComposer(int roomId, int playerId) {
        this.roomId = roomId;
        this.playerId = playerId;
    }

    @Override
    public short getId() {
        return Composers.RoomPlayerUnbannedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(roomId);
        msg.writeInt(playerId);
    }
}
