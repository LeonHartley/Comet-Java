package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class GiveRoomRightsMessageComposer extends MessageComposer {
    private final int roomId;
    private final int playerId;
    private final String username;

    public GiveRoomRightsMessageComposer(int roomId, int playerId, String username) {
        this.roomId = roomId;
        this.playerId = playerId;
        this.username = username;
    }

    @Override
    public short getId() {
        return Composers.FlatControllerAddedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(roomId);
        msg.writeInt(playerId);
        msg.writeString(username);
    }
}
