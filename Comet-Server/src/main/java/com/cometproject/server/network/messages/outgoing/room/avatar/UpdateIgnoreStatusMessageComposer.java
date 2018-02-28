package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class UpdateIgnoreStatusMessageComposer extends MessageComposer {
    private final int result;
    private final String username;

    public UpdateIgnoreStatusMessageComposer(final int result, final String username) {
        this.result = result;
        this.username = username;
    }

    @Override
    public short getId() {
        return Composers.IgnoreStatusMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(result);
        msg.writeString(username);
    }
}
