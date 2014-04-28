package com.cometproject.server.network.clients;

import com.cometproject.server.network.messages.outgoing.misc.PingMessageComposer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

/**
 * Created by Matty on 28/04/2014.
 */
public class ClientIdleHandler extends IdleStateAwareChannelHandler {
    @Override
    public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent ev) {
        if (ev.getState() == IdleState.READER_IDLE) {
            ctx.getChannel().close();
        } else if (ev.getState() == IdleState.WRITER_IDLE) {
            ctx.getChannel().write(PingMessageComposer.compose().get());
        }
    }
}
