package com.cometproject.server.network.messages.outgoing.room.alerts;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class DoorbellNoAnswerComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.FlatAccessDeniedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString("");
    }
}
