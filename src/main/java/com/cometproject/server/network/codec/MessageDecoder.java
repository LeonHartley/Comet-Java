package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferFactory;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class MessageDecoder extends FrameDecoder {
    @Override
    public Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        try {
            if (buffer.readableBytes() < 6) {
                return null;
            }

            if(channel.getAttachment() instanceof Session) {
                Session session = (Session)channel.getAttachment();

                if(session.getEncryption() != null) {
                    session.getEncryption().parse(buffer);
                }
            }

            buffer.markReaderIndex();

            int length = buffer.readInt();

            if (!(buffer.readableBytes() >= length)) {
                buffer.resetReaderIndex();
                return null;
            }

            return new Event(buffer.readBytes(length));
        } catch (Exception e) {
            // TODO: do something with this exception!
            return null;
        }
    }
}