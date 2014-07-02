package com.cometproject.server.network;

import com.cometproject.server.network.clients.ClientHandler;
import com.cometproject.server.network.codec.MessageDecoder;
import com.cometproject.server.network.codec.MessageEncoder;
import com.cometproject.server.network.codec.XMLPolicyDecoder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class NetworkChannelInitializer extends ChannelInitializer<Channel> {
    private final EventExecutorGroup executor;

    public NetworkChannelInitializer(int threadSize) {
        if (threadSize == 0) {
            threadSize = (Runtime.getRuntime().availableProcessors() * 2);
        }
        this.executor = new DefaultEventExecutorGroup(threadSize, new ThreadFactoryBuilder().setNameFormat("Netty Event Thread #%1$d").build());
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast("xmlDecoder", new XMLPolicyDecoder())
                .addLast("messageDecoder", new MessageDecoder())
                .addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8))
                .addLast("messageEncoder", new MessageEncoder())
//                .addLast("idleHandler", new IdleStateHandler(60, 30, 0, TimeUnit.SECONDS))
                .addLast(this.executor, "clientHandler", new ClientHandler());
    }
}