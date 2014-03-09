package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.types.Event;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try {
            if (msg.readableBytes() < 6) {
                return;
            }

            msg.markReaderIndex();
            int length = msg.readInt();

            if (!(msg.readableBytes() >= length)) {
                msg.resetReaderIndex();
                return;
            }

            length -= 2;

            out.add(new Event(msg.readShort(), msg.readBytes(length)));
        } catch (Exception e) {
            e.printStackTrace(); // for debugging!
        }

        /*try {
            if(msg.readableBytes() < 6) {
                return;
            }

            byte[] length = msg.readBytes(4).array();

            if(length[0] == 60) {
                msg.discardReadBytes();

                ctx.writeAndFlush(
                        "<?xml version=\"1.0\"?>\r\n"
                                + "<!DOCTYPE cross-domain-policy SYSTEM \"/xml/dtds/cross-domain-policy.dtd\">\r\n"
                                + "<cross-domain-policy>\r\n"
                                + "<allow-access-from domain=\"*\" to-ports=\"*\" />\r\n"
                                + "</cross-domain-policy>\0"
                ).addListener(ChannelFutureListener.CLOSE);
            } else {
                int messageLength = ByteBuffer.wrap(length).asIntBuffer().get();
                ByteBuf msgBuffer = msg.readBytes(messageLength);

                short header = msgBuffer.readShort();
                out.add(new Event(header, msgBuffer));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }*/
    }
}
