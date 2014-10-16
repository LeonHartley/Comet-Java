package com.cometproject.server.network.messages.types;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import java.nio.charset.Charset;

public class Composer {
    private final static Logger log = Logger.getLogger(Composer.class);

    protected final int id;
    protected ChannelBuffer body;

    public Composer(int id) {
        this.id = id;
        this.body = ChannelBuffers.dynamicBuffer(8192);

        try {
            this.body.writeInt(-1); // reserve this space for message length
            this.body.writeShort(id);
        } catch (Exception e) {
            exceptionCaught(e);
        }
    }

    public Composer(int id, ChannelBuffer buf) {
        this.id = id;
        this.body = buf;
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

    public void writeByte(int b) {
        try {
            this.body.writeByte(b);
        } catch (Exception e) {
            exceptionCaught(e);
        }
    }

    public ChannelBuffer get() {
        if (!this.hasLength()) {
            body.setInt(0, body.writerIndex() - 4);
        }

        return this.body;
    }

    protected static void exceptionCaught(Throwable t) {
        log.error("Error whilst writing data to a composer", t);
    }
}