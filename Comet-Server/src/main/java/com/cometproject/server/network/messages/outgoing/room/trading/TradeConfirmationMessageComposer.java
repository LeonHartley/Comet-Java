package com.cometproject.server.network.messages.outgoing.room.trading;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;


public class TradeConfirmationMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.TradeConfirmationMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
