package com.cometproject.server.network.messages.outgoing.messenger;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class InstantChatMessageComposer {
    public static Composer compose(String message, int fromId) {
        Composer msg = new Composer(Composers.ConsoleChatMessageComposer);

        msg.writeInt(fromId);
        msg.writeString(message);
        msg.writeInt(0);

        return msg;
    }
}
