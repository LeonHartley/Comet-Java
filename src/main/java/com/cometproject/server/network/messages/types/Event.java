package com.cometproject.server.network.messages.types;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import java.nio.charset.Charset;

public class Event {
    private short id;
    private ChannelBuffer buffer;

    public Event(short id, ChannelBuffer buffer) {
        this.id = id;
        this.buffer = (buffer == null || buffer.readableBytes() == 0) ? ChannelBuffers.EMPTY_BUFFER : buffer;
    }

    public int readShort() {
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
        String body = new String(this.getBuffer().toString((Charset.defaultCharset())));
        String data = body;

        for (int i = 0; i < 13; i++) {
            data = data.replace(Character.toString((char) i), "[" + i + "]");
        }

        return data;
    }

    public short getId() {
        return this.id;
    }

    public ChannelBuffer getBuffer() {
        return this.buffer;
    }
}
