package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.types.Event;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

@ChannelHandler.Sharable
public class MessageDecoder extends FrameDecoder {
    private static final Logger log = Logger.getLogger(MessageDecoder.class);

    @Override
    public Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        try {
            if (buffer.readableBytes() < 6) {
                return null;
            }

            buffer.markReaderIndex();
            int length = buffer.readInt();

            if (!(buffer.readableBytes() >= length)) {
                buffer.resetReaderIndex();
                return null;
            }

            if(length < 0) {
                return null;
            }

            return new Event(buffer.readBytes(length));
        } catch (Exception e) {
            log.error("Error while decoding message", e);
        }

        return null;
    }
}