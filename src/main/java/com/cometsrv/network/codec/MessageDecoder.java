package com.cometsrv.network.codec;

import com.cometsrv.network.messages.types.Event;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteBuffer;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> objects) throws Exception {
        try {
            if(byteBuf.readableBytes() < 6) {
                return;
            }

            byte[] length = byteBuf.readBytes(4).array();

            if(length[0] == 60) {
                byteBuf.discardReadBytes();

                channelHandlerContext.channel().write(
                        "<?xml version=\"1.0\"?>\r\n"
                                + "<!DOCTYPE cross-domain-policy SYSTEM \"/xml/dtds/cross-domain-policy.dtd\">\r\n"
                                + "<cross-domain-policy>\r\n"
                                + "<allow-access-from domain=\"*\" to-ports=\"*\" />\r\n"
                                + "</cross-domain-policy>\0"
                ).addListener(ChannelFutureListener.CLOSE);

                System.out.println("Sent policy");
            } else {
                int messageLength = ByteBuffer.wrap(length).asIntBuffer().get();
                ByteBuf msgBuffer = byteBuf.readBytes(messageLength);

                short header = msgBuffer.readShort();
                objects.add(new Event(header, msgBuffer));
            }
        } catch(Exception e) { }
    }
}
