package com.cometsrv.network;

import com.cometsrv.network.clients.ClientHandler;
import com.cometsrv.network.codec.CometStringEncoder;
import com.cometsrv.network.codec.MessageDecoder;
import com.cometsrv.network.codec.MessageEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class NetworkChannelInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        // Shared encoders/decoders
        pipeline.addLast("stringEncoder", new CometStringEncoder());
        pipeline.addLast("encoder", new MessageEncoder());
        pipeline.addLast("decoder", new MessageDecoder());

        // Per channel handler
        pipeline.addLast("handler", new ClientHandler());
    }
}
