package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.google.common.collect.Lists;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferFactory;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import java.util.List;

public class MessageDecoder extends FrameDecoder {
    @Override
    public List<Object> decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        List<Object> packets = Lists.newArrayList();

        final int packetIndex = 0;
//        if(channel.getAttachment() instanceof Session) {
//            Session session = (Session)channel.getAttachment();
//
//            if(session.getEncryption() != null) {
//                session.getEncryption().parse(buffer);
//            }
//        }

        while(buffer.readableBytes() > packetIndex) {
            if(buffer.readableBytes() < 4) break;

            try {
                int length = buffer.readInt();

                if (length >= 2 && length <= 1024) {
                    if (!(buffer.readableBytes() >= length)) {
                        continue;
                    }

                    packets.add(new Event(buffer.readBytes(length)));
                }
            } catch (Exception e) {
//                // TODO: do something with this exception!
//                e.printStackTrace();
            }
        }

        if(packets.size() == 0) return null;

        return packets;
    }
}