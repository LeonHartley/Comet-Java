package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class LeaveRoomMessageComposer extends MessageComposer {
    private final int playerId;

    public LeaveRoomMessageComposer(final int playerId) {
        this.playerId = playerId;
    }

    @Override
    public short getId() {
        return Composers.UserLeftRoomMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(playerId);
    }
}
