package com.cometsrv.network.messages.types;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;

public class Composer {
	private int id;
	private ChannelBufferOutputStream stream;
	private ChannelBuffer body;
	
	public Composer() {}
	
	public Composer(int id) {
		this.init(id);
	}
	
	public Composer init(int id) {
		this.id = id;
		this.body = ChannelBuffers.dynamicBuffer();
		this.stream = new ChannelBufferOutputStream(body);
		
		try {
			this.getStream().writeInt(0);
			this.getStream().writeShort(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return this;
	}
	
	public void writeString(Object obj) {
		try {
			this.getStream().writeUTF(obj.toString());
		} catch(Exception e) {}
	}
	
	public void writeDouble(double d) {
		this.writeString(Double.toString(d));
	}
	
	public void writeInt(int i) {
		try {
			this.getStream().writeInt(i);
		} catch(Exception e) {}
	}

    public void writeLong(long i) {
        try {
            this.getStream().writeLong(i);
        } catch(Exception e) {}
    }


    public void writeBoolean(Boolean b) {
		try {
			this.getStream().writeBoolean(b);
		} catch(Exception e) {}
	}
	
	public void writeShort(int s) {
		try {
			this.getStream().writeShort((short) s);
		} catch(Exception e) {}
	}

	public ChannelBuffer get() {
		body.setInt(0, body.writerIndex() - 4);
		return this.body;
	}
	
	public int getId() {
		return this.id;
	}
	
	public ChannelBufferOutputStream getStream() {
		return this.stream;
	}
}
