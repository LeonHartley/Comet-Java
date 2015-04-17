package com.cometproject.server.network.messages.outgoing.room.alerts;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class RoomConnectionErrorMessageComposer extends MessageComposer {
    private final int errorCode;
    private final String extras;

    public RoomConnectionErrorMessageComposer(final int errorCode, final String extras) {
        this.errorCode = errorCode;
        this.extras = extras;
    }

    @Override
    public short getId() {
        return Composers.RoomConnectionErrorMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(errorCode);

        if (!extras.isEmpty())
            msg.writeString(extras);

    }
}
