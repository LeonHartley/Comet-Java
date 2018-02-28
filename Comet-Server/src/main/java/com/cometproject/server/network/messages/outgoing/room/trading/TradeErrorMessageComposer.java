package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class TradeErrorMessageComposer extends MessageComposer {
    private final int errorCode;
    private final String extras;

    public TradeErrorMessageComposer(int errorCode, String extras) {
        this.errorCode = errorCode;
        this.extras = extras;
    }

    @Override
    public short getId() {
        return Composers.TradingErrorMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(errorCode);
        msg.writeString(extras);
    }
}
