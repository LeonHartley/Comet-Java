package com.cometproject.server.network.codec;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.NetworkEngine;
import com.cometproject.server.network.messages.headers.Events;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.sessions.SessionManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    private Logger log = Logger.getLogger(MessageDecoder.class.getName());

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        try {
            if (msg.readableBytes() < 6) {
                return;
            }

            log.debug("Data received! [length: " + msg.readableBytes() + "]");

            Session client = ctx.channel().attr(NetworkEngine.SESSION_ATTRIBUTE_KEY).get();
            boolean needsDecrypt = true;

            if(client == null || client.getRC4() == null) {
                needsDecrypt = false;
            }

            if(needsDecrypt) {
                msg = Unpooled.copiedBuffer(client.getRC4().Parse(msg.array()));
            }

            msg.markReaderIndex();
            int length = msg.readInt();

            if (!(msg.readableBytes() >= length)) {
                msg.resetReaderIndex();
                return;
            }

            short messageId = msg.readShort();

            length -= 2;

            out.add(new Event(messageId, msg.readBytes(length)));
        } catch (Exception e) {
            e.printStackTrace(); // for debugging!
        }
    }
}
