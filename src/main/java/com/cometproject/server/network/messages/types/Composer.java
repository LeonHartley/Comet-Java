package com.cometproject.server.network.messages.types;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class Composer {
    private int id;
    private ByteBuf body;

    public Composer() {
    }

    public Composer(int id) {
        this.init(id);
    }

    public Composer init(int id) {
        this.id = id;
        this.body = Unpooled.buffer();

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
            //this.body.writeUTF(obj.toString());
            String s = "";
            if (obj != null) {
                s = obj.toString();
            }

            byte[] dat = s.getBytes();
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
            this.body.writeBoolean(b);
        } catch (Exception e) {
        }
    }

    public void writeShort(int s) {
        try {
            this.body.writeShort((short) s);
        } catch (Exception e) {
        }
    }

    private boolean finalized = false;

    public ByteBuf get() {
        if (!finalized) {
            body.setInt(0, body.writerIndex() - 4);
            finalized = true;
        }

        return this.body;//.copy();
    }

    public int getId() {
        return this.id;
    }

	/*public ChannelBufferOutputStream getStream() {
        return this.stream;
	}*/
}
