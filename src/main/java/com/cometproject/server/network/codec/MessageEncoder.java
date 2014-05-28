package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.types.Composer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public final class MessageEncoder extends MessageToByteEncoder<Composer> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Composer composer, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(composer.get());
        byteBuf.release();
    }
}
