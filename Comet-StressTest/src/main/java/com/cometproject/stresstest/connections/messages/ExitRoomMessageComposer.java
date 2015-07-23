package com.cometproject.stresstest.connections.messages;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Events;

public class ExitRoomMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Events.GoToHotelViewMessageEvent;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
