package com.cometproject.server.network.messages.outgoing.handshake;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class UniqueIDMessageComposer {
    public static Composer compose(String uniqueId) {
        Composer msg = new Composer(Composers.UniqueMachineIDMessageComposer);

        msg.writeString(uniqueId);

        return msg;
    }
}
