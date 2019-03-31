package com.cometproject.server.network.messages.outgoing.misc;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class OpenLinkMessageComposer extends MessageComposer {
    private final String link;

    public OpenLinkMessageComposer(String link) {
        this.link = link;
    }

    @Override
    public short getId() {
        return Composers.OpenLinkMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(link);
    }
}
