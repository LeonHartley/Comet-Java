package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class TradeCompletedMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.TradeCompletedMessageComposer;
    }

    @Override
    public void compose(Composer msg) {

    }
}
