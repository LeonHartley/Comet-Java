package com.cometproject.network;

import com.cometproject.network.clients.ClientHandler;
import com.cometproject.network.codec.CometStringEncoder;
import com.cometproject.network.codec.MessageDecoder;
import com.cometproject.network.codec.MessageEncoder;
import com.cometproject.network.codec.XMLPolicyDecoder;
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
