package com.cometproject.server.network.messages.outgoing.room.alerts;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class RoomErrorMessageComposer extends MessageComposer {
    private final int errorCode;

    public RoomErrorMessageComposer(final int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public short getId() {
        return Composers.GenericErrorMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.errorCode);
    }
}
