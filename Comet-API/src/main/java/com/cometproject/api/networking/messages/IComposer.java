package com.cometproject.api.networking.messages;

public interface IComposer {
    int getId();

    void clear();

    boolean isFinalised();

    void writeString(Object obj);

    void writeDouble(double d);

    void writeInt(int i);

    void writeLong(long i);

    void writeBoolean(Boolean b);

    void writeByte(int b);

    void writeShort(int s);
}
