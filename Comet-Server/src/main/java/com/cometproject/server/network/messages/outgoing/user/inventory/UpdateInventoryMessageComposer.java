package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class UpdateInventoryMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.FurniListUpdateMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
