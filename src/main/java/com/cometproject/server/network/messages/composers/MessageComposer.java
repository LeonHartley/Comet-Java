package com.cometproject.server.network.messages.composers;

import io.netty.buffer.ByteBuf;

public abstract class MessageComposer {
    private final short header;
    public MessageComposer(short header) {
        this.header = header;
    }

    public final void writeMessage(ByteBuf buffer) {
        // write the header
        buffer.writeShort(this.getHeader());

    }

    public abstract void compose(ByteBuf buffer);

    public short getHeader() {
        return this.header;
    }
}
