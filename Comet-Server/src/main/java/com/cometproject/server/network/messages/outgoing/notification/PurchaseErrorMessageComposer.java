package com.cometproject.server.network.messages.outgoing.notification;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class PurchaseErrorMessageComposer extends MessageComposer {

    private final int ErrorCode;

    public PurchaseErrorMessageComposer(final int ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    @Override
    public short getId() {
        return Composers.PurchaseErrorMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(ErrorCode);
    }
}
