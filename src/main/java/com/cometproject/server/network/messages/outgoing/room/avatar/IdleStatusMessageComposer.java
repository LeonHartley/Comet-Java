package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class IdleStatusMessageComposer extends MessageComposer {
    private final int playerId;
    private final boolean isIdle;

    public IdleStatusMessageComposer(final int playerId, final boolean isIdle) {
        this.playerId = playerId;
        this.isIdle = isIdle;
    }

    @Override
    public short getId() {
        return Composers.RoomUserIdleMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(this.playerId);
        msg.writeBoolean(this.isIdle);
    }
}
