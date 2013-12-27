package com.cometsrv.network.codec;

import com.cometsrv.network.messages.types.Event;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import java.nio.ByteBuffer;

public class MessageDecoder extends FrameDecoder {

    private static Logger log = Logger.getLogger(MessageDecoder.class.getName());

    @Override
    public Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) {
        try {
            if(buffer.readableBytes() < 6) {
                return null;
            }

            byte[] length = buffer.readBytes(4).array();

            if(length[0] == 60) {
                buffer.discardReadBytes();

                channel.write(
                    "<?xml version=\"1.0\"?>\r\n"
                    + "<!DOCTYPE cross-domain-policy SYSTEM \"/xml/dtds/cross-domain-policy.dtd\">\r\n"
                    + "<cross-domain-policy>\r\n"
                    + "<allow-access-from domain=\"*\" to-ports=\"*\" />\r\n"
                    + "</cross-domain-policy>\0"
                );
            } else {
                int messageLength = ByteBuffer.wrap(length).asIntBuffer().get();
                ChannelBuffer msgBuffer = buffer.readBytes(messageLength);

                short header = msgBuffer.readShort();
                return new Event(header, msgBuffer);
            }
        } catch(Exception e) { }
        return null;
    }
}
