package com.cometproject.server.network.codec;

import com.cometproject.server.network.messages.types.Composer;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;

import java.nio.charset.Charset;

/**
 * Created by Matty on 28/04/2014.
 */
public class MessageEncoder extends SimpleChannelDownstreamHandler {
    private static Logger log = Logger.getLogger(MessageEncoder.class.getName());

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent ev) throws Exception {
        if (ev.getMessage() instanceof Composer) {
            Composer msg = (Composer) ev.getMessage();
            Channels.write(ctx, ev.getFuture(), msg.get());
        } else if (ev.getMessage() instanceof String) {
            Channels.write(ctx, ev.getFuture(), ChannelBuffers.copiedBuffer((String) ev.getMessage(), Charset.forName("UTF-8")));
        } else if (ev.getMessage() instanceof ChannelBuffer) {
            Channels.write(ctx, ev.getFuture(), ev.getMessage());
        } else {
            log.warn("Unable to encode a message : " + ev.getMessage().getClass());
        }
    }
}
