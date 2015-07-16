package com.cometproject.server.network.messages.outgoing.landing;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class HotelViewItemMessageComposer extends MessageComposer {
    private final String key;
    private final String value;

    public HotelViewItemMessageComposer(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public short getId() {
        return Composers.LandingWidgetMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.key);
        msg.writeString(this.value);
    }
}
