package com.cometproject.server.network.messages.types;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.Unpooled;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;

public final class Event implements ByteBufHolder {
    private final static Logger log = Logger.getLogger(Event.class);

    private final short id;
    private final ByteBuf buffer;

    public Event(ByteBuf buf) {
        this.buffer = (buf != null) && (buf.readableBytes() > 0) ? buf : Unpooled.EMPTY_BUFFER;

        if (this.content().readableBytes() >= 2) {
            this.id = this.readShort();
        } else {
            this.id = 0;
        }
    }

    @Override
    public ByteBuf content() {
        return this.buffer;
    }

    @Override
    public ByteBufHolder copy() {
        return new Event(this.buffer.copy());
    }

    @Override
    public ByteBufHolder duplicate() {
        return new Event(this.buffer.duplicate());
    }

    @Override
    public int refCnt() {
        return this.buffer.refCnt();
    }

    @Override
    public ByteBufHolder retain() {
        return new Event(this.buffer.retain());
    }

    @Override
    public ByteBufHolder retain(int increment) {
        return new Event(this.buffer.retain(increment));
    }

    @Override
    public ByteBufHolder touch() {
        return null;
    }

    @Override
    public ByteBufHolder touch(Object o) {
        return null;
    }

    @Override
    public boolean release() {
        return this.buffer.release();
    }

    @Override
    public boolean release(int decrement) {
        return this.buffer.release(decrement);
    }

    public short readShort() {
        return this.content().readShort();
    }

    public int readInt() {
        try {
            return this.content().readInt();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean readBoolean() {
        return this.content().readByte() == 1;
    }

    public String readString() {
        int length = this.readShort();
        byte[] data = this.content().readBytes((length)).array();

        return new String(data);
    }

    public String toString() {
        String body = this.content().toString((Charset.defaultCharset()));

        for (int i = 0; i < 13; i++) {
            body = body.replace(Character.toString((char) i), "[" + i + "]");
        }

        return body;
    }

    public short getId() {
        return this.id;
    }
}
