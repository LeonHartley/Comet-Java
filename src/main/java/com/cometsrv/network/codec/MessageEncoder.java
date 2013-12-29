package com.cometsrv.network.codec;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;
import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import java.nio.charset.Charset;

public class MessageEncoder extends SimpleChannelHandler {
    private static Logger log = Logger.getLogger(MessageEncoder.class.getName());

    public void writeRequested(ChannelHandlerContext ctx, MessageEvent event) {
        try {
            if(event.getMessage() instanceof String) {
                Channels.write(ctx, event.getFuture(), ChannelBuffers.copiedBuffer((String) event.getMessage(), Charset.forName("UTF-8")));
            } else if(event.getMessage() instanceof Composer) {

                Composer msg = (Composer)event.getMessage();
                Channels.write(ctx, event.getFuture(), msg.get());

                log.debug("Composed message: " + Composers.valueOfId(msg.getId()));

            }
        } catch(Exception e) {
            log.error("Failed to encode message", e);
        }
    }
}
