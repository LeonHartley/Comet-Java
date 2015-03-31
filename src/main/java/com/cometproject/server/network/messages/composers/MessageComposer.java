package com.cometproject.server.network.messages.composers;

import com.cometproject.server.network.messages.types.Composer;
import io.netty.buffer.ByteBuf;

public abstract class MessageComposer {
    private boolean isCancelled = false;

    public MessageComposer() {
    }

    public final Composer writeMessage() {
        final Composer composer = new Composer(this.getId());

        // Do anything we need to do with the buffer.

        try {
            this.compose(composer);
        } finally {
            this.dispose();
        }

        return composer;
    }

    public abstract short getId();

    public abstract void compose(Composer msg);

    public void dispose() {

    }
}
