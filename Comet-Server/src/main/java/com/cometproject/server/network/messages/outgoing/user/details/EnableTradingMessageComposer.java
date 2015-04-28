package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class EnableTradingMessageComposer extends MessageComposer {

    private final boolean tradingEnabled;

    public EnableTradingMessageComposer(final boolean tradingEnabled) {
        this.tradingEnabled = tradingEnabled;
    }

    @Override
    public short getId() {
        return Composers.EnableTradingMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeBoolean(this.tradingEnabled);
    }
}
