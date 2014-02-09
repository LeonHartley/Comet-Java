package com.cometsrv.network.codec;

import com.cometsrv.network.NetworkEngine;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteBuffer;
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

            Session session = ctx.channel().attr(NetworkEngine.SESSION_ATTRIBUTE_KEY).get();

            ByteBuf decryptedBytes = msg.readBytes(length);

            if(session != null && session.getEncryption() != null && session.getEncryption().isInitialized()) {
                decryptedBytes = session.getEncryption().decipher(decryptedBytes);
            }

            out.add(new Event(decryptedBytes.readShort(), decryptedBytes));
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
