package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class TradeErrorMessageComposer extends MessageComposer {
    private final int errorCode;
    private final String extras;

    public TradeErrorMessageComposer(int errorCode, String extras) {
        this.errorCode = errorCode;
        this.extras = extras;
    }

    @Override
    public short getId() {
        return Composers.TradeErrorMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(errorCode);
        msg.writeString(extras);
    }
}
