package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.types.Event;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;

import java.util.List;

public final class MessageDecoder extends ByteToMessageDecoder {
    public MessageDecoder() {
        super();
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> objects) throws Exception {
        if (byteBuf.readableBytes() < 6) {
            return;
        }

        byteBuf.markReaderIndex();
        int length = byteBuf.readInt();

        if (!(byteBuf.readableBytes() >= length)) {
            byteBuf.resetReaderIndex();
            return;
        }

        objects.add(new Event(byteBuf.readBytes(length)));
    }
}
