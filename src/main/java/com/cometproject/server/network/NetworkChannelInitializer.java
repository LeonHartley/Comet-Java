package com.cometproject.server.network;

import com.cometproject.server.network.clients.ClientHandler;
import com.cometproject.server.network.codec.MessageDecoder;
import com.cometproject.server.network.codec.MessageEncoder;
import com.cometproject.server.network.codec.XMLPolicyDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

public class NetworkChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final EventExecutorGroup eventExecutor;

    public NetworkChannelInitializer(int threadSize) {
        this.eventExecutor = new DefaultEventExecutorGroup(threadSize);
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast("xmlDecoder", new XMLPolicyDecoder());
        socketChannel.pipeline().addLast("messageDecoder", new MessageDecoder());
        socketChannel.pipeline().addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
        socketChannel.pipeline().addLast("messageEncoder", new MessageEncoder());

        socketChannel.pipeline().addLast("idleHandler", new IdleStateHandler(60, 30, 0, TimeUnit.SECONDS));
        socketChannel.pipeline().addLast("mainHandler", new ClientHandler());
    }
}
