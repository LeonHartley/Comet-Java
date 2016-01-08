package com.cometproject.server.network.messages.outgoing.handshake;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class HomeRoomMessageComposer extends MessageComposer {
    private final int roomId;

    public HomeRoomMessageComposer(final int roomId) {
        this.roomId = roomId;
    }

    @Override
    public short getId() {
        return Composers.NavigatorSettingsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.roomId);
        msg.writeInt(this.roomId);
    }
}
