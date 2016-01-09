package com.cometproject.server.network.messages.outgoing.navigator.updated;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class NavigatorLiftedRoomsMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.NavigatorLiftedRoomsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(0);
    }
}
