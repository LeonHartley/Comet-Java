package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class InstantChatMessageComposer extends MessageComposer {
    private final String message;
    private final int fromId;

    public InstantChatMessageComposer(final String message, final int fromId) {
        this.message = message;
        this.fromId = fromId;
    }

    @Override
    public short getId() {
        return Composers.ConsoleChatMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(fromId);
        msg.writeString(message);
        msg.writeInt(0);
    }
}
