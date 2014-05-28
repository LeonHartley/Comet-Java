package com.cometproject.server.network.messages.types;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public final class Event {
    private final short id;
    private final ByteBuf buffer;

    public Event(int length, ByteBuf buf) {
        try {
            this.buffer =
                    (length > 0) && (buf.readableBytes() > 0) ?
                            buf.alloc().buffer(length).writeBytes(buf) :
                            Unpooled.EMPTY_BUFFER;

            if (this.buffer.readableBytes() >= 2) {
                this.id = this.readShort();
            } else {
                this.id = 0;
            }
        } finally {
            buf.release();
        }
    }

    public short readShort() {
        return this.getBuffer().readShort();
    }

    public int readInt() {
        try {
            return this.getBuffer().readInt();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean readBoolean() {
        return this.getBuffer().readByte() == 1;
    }

    public String readString() {
        int length = this.readShort();
        byte[] data = this.getBuffer().readBytes((length)).array();

        return new String(data);
    }

    public String toString() {
        String body = this.getBuffer().toString((Charset.defaultCharset()));

        for (int i = 0; i < 13; i++) {
            body = body.replace(Character.toString((char) i), "[" + i + "]");
        }

        return body;
    }

    public short getId() {
        return this.id;
    }

    public ByteBuf getBuffer() {
        return this.buffer;
    }
}
