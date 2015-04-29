package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;


public class RemoveRightsMessageComposer extends MessageComposer {
    private final int playerId;
    private final int roomId;

    public RemoveRightsMessageComposer(final int playerId, final int roomId) {
        this.playerId = playerId;
        this.roomId = roomId;
    }

    @Override
    public short getId() {
        return Composers.RemoveRightsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(roomId);
        msg.writeInt(playerId);
    }
}
