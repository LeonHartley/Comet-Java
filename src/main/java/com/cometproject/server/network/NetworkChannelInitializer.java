package com.cometproject.server.network;

import com.cometproject.server.network.clients.ClientHandler;
import com.cometproject.server.network.codec.MessageDecoder;
import com.cometproject.server.network.codec.MessageEncoder;
import com.cometproject.server.network.codec.XMLPolicyDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;

public class NetworkChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final EventExecutorGroup executor;

    public NetworkChannelInitializer(EventExecutorGroup executorGroup) {
        this.executor = executorGroup;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.config().setTrafficClass(0x18);

        ch.pipeline()
                .addLast("xmlDecoder", new XMLPolicyDecoder())
                .addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8))
                .addLast("messageDecoder", new MessageDecoder())
                .addLast("messageEncoder", new MessageEncoder())
//                .addLast("idleHandler", new IdleStateHandler(60, 30, 0, TimeUnit.SECONDS))
                .addLast(this.executor, "clientHandler", ClientHandler.getInstance());
    }
}