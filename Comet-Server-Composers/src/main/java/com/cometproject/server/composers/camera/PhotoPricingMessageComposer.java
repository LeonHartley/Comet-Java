package com.cometproject.server.composers.camera;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class PhotoPricingMessageComposer extends MessageComposer {
    private final int coins;
    private final int duckets;

    public PhotoPricingMessageComposer(int coins, int duckets) {
        this.coins = coins;
        this.duckets = duckets;
    }

    @Override
    public short getId() {
        return Composers.PhotoPriceMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.coins);
        msg.writeInt(this.duckets);
    }
}
