package com.cometproject.server.network;

import com.cometproject.server.network.clients.ClientHandler;
import com.cometproject.server.network.codec.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class NetworkChannelInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast("stringEncoder", new CometStringEncoder());
        pipeline.addLast("encoder", new MessageEncoder());
        pipeline.addLast("xmlDecoder", new XMLPolicyDecoder());
        pipeline.addLast("decoder", new MessageDecoder());
        pipeline.addLast("handler", new ClientHandler());
    }
}
