package com.cometproject.stresstest.connections.messages;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Events;

public class WalkMessageComposer extends MessageComposer {
    private int x;
    private int y;

    public WalkMessageComposer(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public short getId() {
        return Events.MoveAvatarMessageEvent;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(x);
        msg.writeInt(y);
    }
}
