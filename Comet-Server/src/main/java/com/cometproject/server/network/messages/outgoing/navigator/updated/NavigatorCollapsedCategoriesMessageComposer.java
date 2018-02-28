package com.cometproject.server.network.messages.outgoing.navigator.updated;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class NavigatorCollapsedCategoriesMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.NavigatorCollapsedCategoriesMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(0);
    }
}
