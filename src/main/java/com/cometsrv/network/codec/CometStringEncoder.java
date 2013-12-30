package com.cometsrv.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

public class CometStringEncoder extends MessageToMessageEncoder<CharSequence> {
    @Override
    protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
        ctx.write(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), Charset.forName("UTF-8")));
    }
}
