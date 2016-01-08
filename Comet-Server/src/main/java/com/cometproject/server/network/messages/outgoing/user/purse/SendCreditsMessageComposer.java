package com.cometproject.server.network.messages.outgoing.user.purse;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class SendCreditsMessageComposer extends MessageComposer {
    private final int credits;

    public SendCreditsMessageComposer(final int credits) {
        this.credits = credits;
    }

    @Override
    public short getId() {
        return Composers.CreditBalanceMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(credits + ".0");
    }
}
