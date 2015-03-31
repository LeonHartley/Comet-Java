package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

@ChannelHandler.Sharable
public class MessageEncoder extends SimpleChannelDownstreamHandler {
    private static Logger log = Logger.getLogger(MessageEncoder.class.getName());

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent ev) throws Exception {
        if (ev.getMessage() instanceof MessageComposer) {
            Composer msg = ((MessageComposer) ev.getMessage()).writeMessage();
            Channels.write(ctx, ev.getFuture(), msg.get());
        } else if (ev.getMessage() instanceof ChannelBuffer) {
            Channels.write(ctx, ev.getFuture(), ev.getMessage());
        } else {
            log.warn("Unable to encode a message: " + ev.getMessage().getClass());
        }
    }
}