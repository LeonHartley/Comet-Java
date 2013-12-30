package com.cometsrv.network.codec;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;

public class MessageEncoder extends MessageToByteEncoder<Composer> {
    private static Logger log = Logger.getLogger(MessageEncoder.class.getName());

    @Override
    protected void encode(ChannelHandlerContext ctx, Composer composer, ByteBuf byteBuf) throws Exception {
        try {
            //byteBuf.writeBytes(composer.get());
            ctx.writeAndFlush(composer.get());

            log.debug("Composed message: " + Composers.valueOfId(composer.getId()));
        } catch(Exception e) {
            log.error("Failed to encode message : ", e);
        }
    }
}
