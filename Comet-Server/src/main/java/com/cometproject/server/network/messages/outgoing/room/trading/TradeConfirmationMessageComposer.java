package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class TradeConfirmationMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.TradeConfirmationMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
