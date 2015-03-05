package com.cometproject.server.network.messages.composers;

import com.cometproject.server.network.messages.types.Composer;
import io.netty.buffer.ByteBuf;

public abstract class MessageComposer {
    public MessageComposer() {
    }

    public final void writeMessage(ByteBuf buffer) {
        final Composer composer = new Composer(this.getId(), buffer);

        // Do anything we need to do with the buffer.

        this.compose(composer);
    }

    public abstract short getId();

    public abstract void compose(Composer msg);
}
