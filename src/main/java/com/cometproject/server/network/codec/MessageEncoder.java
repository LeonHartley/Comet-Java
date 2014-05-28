package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.types.Composer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public final class MessageEncoder extends MessageToByteEncoder<Composer> {
    public MessageEncoder() {
        super(false);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Composer composer, ByteBuf byteBuf) throws Exception {
        try {
            if (composer.get().isReadable() && composer.get().readableBytes() > 0) {
                byteBuf.writeBytes(composer.get());
            }
        } finally {
            composer.get().release();
        }
    }
}
