package com.cometproject.server.network.messages.outgoing.landing;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


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
    public void compose(Composer msg) {
        msg.writeString(this.key);
        msg.writeString(this.value);
    }
}
