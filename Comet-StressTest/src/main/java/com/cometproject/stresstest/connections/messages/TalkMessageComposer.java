package com.cometproject.stresstest.connections.messages;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Events;

public class TalkMessageComposer extends MessageComposer {
    private String message;

    public TalkMessageComposer(String message) {
        this.message = message;
    }

    @Override
    public short getId() {
        return Events.ShoutMessageEvent;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(message);
        msg.writeInt(0);
    }
}
