package com.cometproject.server.network.messages.outgoing.advertisements;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;

public class InterstitialMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.InterstitialMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString("");//imageUrl
        msg.writeString("");//clickUrl
        msg.writeString("");//videoUrl
    }
}
