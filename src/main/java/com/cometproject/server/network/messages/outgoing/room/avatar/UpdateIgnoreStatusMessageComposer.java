package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class UpdateIgnoreStatusMessageComposer extends MessageComposer {
    private final int result;
    private final String username;

    public UpdateIgnoreStatusMessageComposer(final int result, final String username) {
        this.result = result;
        this.username = username;
    }

    @Override
    public short getId() {
        return Composers.UpdateIgnoreStatusMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(result);
        msg.writeString(username);
    }
}
