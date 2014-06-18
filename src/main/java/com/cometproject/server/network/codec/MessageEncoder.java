package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.types.Composer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Composer> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Composer msg, ByteBuf out) throws Exception {
        if (!msg.hasLength()) {
            msg.content().setInt(0, msg.content().writerIndex() - 4);
        }

        out.writeBytes(msg.content());
    }
}
