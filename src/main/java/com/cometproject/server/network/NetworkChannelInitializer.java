package com.cometproject.server.network;

import com.cometproject.server.network.clients.ClientHandler;
import com.cometproject.server.network.clients.ClientIdleHandler;
import com.cometproject.server.network.codec.MessageDecoder;
import com.cometproject.server.network.codec.MessageEncoder;
import com.cometproject.server.network.codec.XMLPolicyDecoder;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timer;

public class NetworkChannelInitializer implements ChannelPipelineFactory {
    private final Timer idleTimer;
    private final ClientIdleHandler idleHandler;

    private final OrderedMemoryAwareThreadPoolExecutor executor;

    public NetworkChannelInitializer(OrderedMemoryAwareThreadPoolExecutor executionExecutor) {
        this.idleTimer = new HashedWheelTimer();
        this.idleHandler = new ClientIdleHandler();

        this.executor = executionExecutor;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();

        //pipeline.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_16));
        pipeline.addLast("xmlDecoder", new XMLPolicyDecoder());
        pipeline.addLast("messageEncoder", new MessageEncoder());
        pipeline.addLast("decoder", new MessageDecoder());
        pipeline.addLast("idleStateHandler", new IdleStateHandler(this.idleTimer, 60, 30, 0));
        pipeline.addLast("clientIdleHandler", this.idleHandler);
        pipeline.addLast("executionHandler", new ExecutionHandler(this.executor));
        pipeline.addLast("handler", new ClientHandler());

        return pipeline;
    }
}
