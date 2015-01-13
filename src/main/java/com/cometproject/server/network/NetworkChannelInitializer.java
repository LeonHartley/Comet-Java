package com.cometproject.server.network;

import com.cometproject.server.network.clients.ClientHandler;
import com.cometproject.server.network.codec.MessageDecoder;
import com.cometproject.server.network.codec.MessageEncoder;
import com.cometproject.server.network.codec.XMLPolicyDecoder;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.util.CharsetUtil;


public class NetworkChannelInitializer implements ChannelPipelineFactory {
    private final ExecutionHandler executionHandler;

    private final StringEncoder stringEncoder = new StringEncoder(CharsetUtil.UTF_8);
    private final MessageDecoder messageDecoder = new MessageDecoder();
    private final MessageEncoder messageEncoder = new MessageEncoder();
    private final ClientHandler clientHandler = new ClientHandler();

    public NetworkChannelInitializer(OrderedMemoryAwareThreadPoolExecutor executionExecutor) {
        this.executionHandler = new ExecutionHandler(executionExecutor);
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();

        pipeline.addLast("xmlDecoder", new XMLPolicyDecoder()); // if this obj isn't created now, it causes issues; TODO: find out why
        pipeline.addLast("messageDecoder", this.messageDecoder);
        pipeline.addLast("messageEncoder", this.messageEncoder);
        pipeline.addLast("stringEncoder", this.stringEncoder);
        pipeline.addLast("executionHandler", this.executionHandler);
        pipeline.addLast("handler", this.clientHandler);

        return pipeline;
    }
}
