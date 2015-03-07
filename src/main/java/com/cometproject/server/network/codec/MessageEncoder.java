package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;

public class MessageEncoder extends MessageToByteEncoder<MessageComposer> {
    private final Logger log = Logger.getLogger(MessageEncoder.class.getName());

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageComposer msg, ByteBuf out) throws Exception {
        final Composer composer = msg.writeMessage(out);

        if (!composer.hasLength()) {
            composer.content().setInt(0, composer.content().writerIndex() - 4);
        }
    }
}