package com.cometproject.server.network.messages.types;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import java.nio.charset.Charset;


public final class Event {
    private final short id;
    private final ChannelBuffer buffer;

    public Event(ChannelBuffer buf) {
        this.buffer = (buf != null) && (buf.readableBytes() > 0) ? buf : ChannelBuffers.EMPTY_BUFFER;

        if (this.buffer.readableBytes() >= 2) {
            this.id = this.readShort();
        } else {
            this.id = 0;
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
        try {
            int length = this.readShort();
            byte[] data = this.getBuffer().readBytes((length)).array();

            return new String(data);
        } catch (Exception e) {
            return null;
        }
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

    public ChannelBuffer getBuffer() {
        return this.buffer;
    }
}