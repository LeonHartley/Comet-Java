package com.cometproject.server.network.messages.types;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;

public final class Composer {
    private final static Logger log = Logger.getLogger(Composer.class);

    private final int id;
    private final ByteBuf body;

    public Composer(int id) {
        this.id = id;
        this.body = ByteBufAllocator.DEFAULT.buffer();

        try {
            this.body.writeInt(-1); // reserve this space for message length
            this.body.writeShort(id);
        } catch (Exception e) {
            exceptionCaught(e);
        }
    }

    public Composer(int id, ByteBuf buf) {
        this.id = id;
        this.body = buf;
    }

    public Composer duplicate() {
        return new Composer(this.id, this.body.duplicate().retain());
    }

    public int getId() {
        return this.id;
    }

    public void clear() {
        this.body.clear();
    }

    public boolean hasLength() {
        return (this.body.getInt(0) > -1);
    }

    public void writeString(Object obj) {
        try {
            String string = "";

            if(obj != null) {
                string = String.valueOf(obj);
            }

            byte[] dat = string.getBytes(Charset.forName("UTF-8"));
            this.body.writeShort(dat.length);
            this.body.writeBytes(dat);
        } catch (Exception e) {
            exceptionCaught(e);
        }
    }

    public void writeDouble(double d) {
        this.writeString(Double.toString(d));
    }

    public void writeInt(int i) {
        try {
            this.body.writeInt(i);
        } catch (Exception e) {
            exceptionCaught(e);
        }
    }

    public void writeLong(long i) {
        try {
            this.body.writeLong(i);
        } catch (Exception e) {
            exceptionCaught(e);
        }
    }

    public void writeBoolean(Boolean b) {
        try {
            this.body.writeByte(b ? 1 : 0);
        } catch (Exception e) {
            exceptionCaught(e);
        }
    }

    public void writeShort(int s) {
        try {
            this.body.writeShort((short) s);
        } catch (Exception e) {
            exceptionCaught(e);
        }
    }

    public ByteBuf get() {
        if (!this.hasLength()) {
            body.setInt(0, body.writerIndex() - 4);
        }

        return this.body;
    }

    protected static void exceptionCaught(Throwable t) {
        log.error("Error whilst writing data to a composer", t);
    }
}
