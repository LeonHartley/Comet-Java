package com.cometproject.server.network.messages.types;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import java.nio.charset.Charset;

public class Composer {
    private int id;
    private ChannelBuffer body;

    private boolean finalized = false;

    public Composer(int id) {
        this.init(id);
    }

    public Composer init(int id) {
        this.id = id;
        this.body = ChannelBuffers.dynamicBuffer();

        try {
            this.body.writeInt(0);
            this.body.writeShort(id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
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
        }
    }

    public void writeDouble(double d) {
        this.writeString(Double.toString(d));
    }

    public void writeInt(int i) {
        try {
            this.body.writeInt(i);
        } catch (Exception e) {
        }
    }

    public void writeLong(long i) {
        try {
            this.body.writeLong(i);
        } catch (Exception e) {
        }
    }

    public void writeBoolean(Boolean b) {
        try {
            this.body.writeByte(b ? 1 : 0);
        } catch (Exception e) {
        }
    }

    public void writeShort(int s) {
        try {
            this.body.writeShort((short) s);
        } catch (Exception e) {
        }
    }

    public ChannelBuffer get() {
        if (!this.finalized) {
            body.setInt(0, body.writerIndex() - 4);
            finalized = true;
        }

        return this.body;
    }

    public int getId() {
        return this.id;
    }
}
