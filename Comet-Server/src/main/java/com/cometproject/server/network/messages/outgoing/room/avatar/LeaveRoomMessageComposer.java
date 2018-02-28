package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class LeaveRoomMessageComposer extends MessageComposer {
    private final int playerId;

    public LeaveRoomMessageComposer(final int playerId) {
        this.playerId = playerId;
    }

    @Override
    public short getId() {
        return Composers.UserRemoveMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(playerId);
    }
}
