package com.cometproject.stresstest.connections.messages;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Events;

public class LoadRoomMessageComposer extends MessageComposer {

    private int roomId;
    private String password;

    public LoadRoomMessageComposer(int roomId, String password) {
        this.roomId = roomId;
        this.password = password;
    }

    @Override
    public short getId() {
        return Events.OpenFlatConnectionMessageEvent;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(roomId);
        msg.writeString(password);
    }
}
