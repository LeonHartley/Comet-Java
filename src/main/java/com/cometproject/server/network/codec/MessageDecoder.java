package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.types.Event;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try {
            if (msg.readableBytes() < 6) {
                return;
            }

            msg.markReaderIndex();
            int length = msg.readInt();

            if (!(msg.readableBytes() >= length)) {
                msg.resetReaderIndex();
                return;
            }

            length -= 2;

            out.add(new Event(msg.readShort(), msg.readBytes(length)));
        } catch (Exception e) {
            e.printStackTrace(); // for debugging!
        }
    }
}
