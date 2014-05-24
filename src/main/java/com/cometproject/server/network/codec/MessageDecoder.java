package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.types.Event;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;

import java.util.List;

public final class MessageDecoder extends ByteToMessageDecoder {
    private static Logger log = Logger.getLogger(ByteToMessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> objects) throws Exception {
        if (byteBuf.readableBytes() < 6) {
            log.trace("Incoming data request but length was less than 6 bytes, waiting for more data");
            return;
        }

        byteBuf.markReaderIndex();
        int length = byteBuf.readInt();

        if (!(byteBuf.readableBytes() >= length)) {
            byteBuf.resetReaderIndex();
            log.trace("Incoming data request but length doesn't match the data length expected, waiting for more data");
            return;
        }

        log.trace("Incoming data request success, passing it to handler");
        objects.add(new Event(byteBuf.readBytes(length)));
    }
}
