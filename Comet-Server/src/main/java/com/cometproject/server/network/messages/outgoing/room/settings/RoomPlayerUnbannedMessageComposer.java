package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class RoomPlayerUnbannedMessageComposer extends MessageComposer {
    private final int roomId;
    private final int playerId;

    public RoomPlayerUnbannedMessageComposer(int roomId, int playerId) {
        this.roomId = roomId;
        this.playerId = playerId;
    }

    @Override
    public short getId() {
        return Composers.UnbanUserFromRoomMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(roomId);
        msg.writeInt(playerId);
    }
}
