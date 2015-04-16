package com.cometproject.server.network.messages.outgoing.room.alerts;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RoomErrorMessageComposer extends MessageComposer {
    private final int errorCode;

    public RoomErrorMessageComposer(final int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public short getId() {
        return Composers.RoomEnterErrorMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(this.errorCode);
    }
}
