package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class TradeCompletedMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.TradingFinishMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
