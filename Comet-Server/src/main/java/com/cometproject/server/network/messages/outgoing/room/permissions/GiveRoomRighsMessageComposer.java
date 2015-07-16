package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class GiveRoomRighsMessageComposer extends MessageComposer {
    private final int roomId;
    private final int playerId;
    private final String username;

    public GiveRoomRighsMessageComposer(int roomId, int playerId, String username) {
        this.roomId = roomId;
        this.playerId = playerId;
        this.username = username;
    }

    @Override
    public short getId() {
        return Composers.GiveRoomRightsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(roomId);
        msg.writeInt(playerId);
        msg.writeString(username);
    }
}
