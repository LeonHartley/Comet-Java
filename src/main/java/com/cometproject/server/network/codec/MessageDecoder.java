package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.types.Event;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 6) {
            return;
        }

        in.markReaderIndex();
        int length = in.readInt();

        if (!(in.readableBytes() >= length)) {
            in.resetReaderIndex();
            return;
        }

        out.add(new Event(in.readBytes(length)));
    }
}