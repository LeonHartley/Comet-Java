package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class CreateRoomMessageComposer extends MessageComposer {
    private final int roomId;
    private final String roomName;

    public CreateRoomMessageComposer(final int roomId, final String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public short getId() {
        return Composers.FlatCreatedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.roomId);
        msg.writeString(this.roomName);
    }
}
