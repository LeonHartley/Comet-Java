package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.types.Event;
import com.google.common.collect.Lists;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import java.util.List;

public class MessageDecoder extends FrameDecoder {
    @Override
<<<<<<< HEAD
    public Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        try {
            if (buffer.readableBytes() < 6) {
                return null;
            }
=======
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
>>>>>>> FETCH_HEAD

            buffer.markReaderIndex();
            int length = buffer.readInt();

            if (!(buffer.readableBytes() >= length)) {
                buffer.resetReaderIndex();
                return null;
            }

            return new Event(buffer.readBytes(length));
        } catch (Exception e) {
            e.printStackTrace(); // for debugging!
        }

        return null;
    }
}