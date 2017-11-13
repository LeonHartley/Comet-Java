package com.cometproject.server.network.messages.outgoing.camera;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class PurchasedPhotoMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.PurchasedPhotoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
