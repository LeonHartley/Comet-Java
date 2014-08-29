package com.cometproject.server.network.monitor;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.net.ConnectException;

public class MonitorClient {
    public final String MONITOR_HOST = "127.0.0.1";
    public final int MONITOR_PORT = 1337;

    private MonitorClientHandler clientHandler;

    private Logger log = Logger.getLogger(MonitorClient.class.getName());

    public MonitorClient(EventLoopGroup loopGroup) {
        this.clientHandler = new MonitorClientHandler();

        Thread monitorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bootstrap bootstrap = new Bootstrap();

                    bootstrap.group(loopGroup)
                            .channel(NioSocketChannel.class)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
//                                    socketChannel.pipeline().addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
                                    socketChannel.pipeline().addLast("clientHandler", clientHandler);
                                }
                            });

                    ChannelFuture future = bootstrap.connect(MONITOR_HOST, MONITOR_PORT).sync();

                    future.channel().closeFuture().sync();
                } catch (Exception e) {
                    log.error("Failed to connect to monitor server", e);
                }
            }
        });

        monitorThread.start();
    }

    public MonitorClientHandler getClientHandler() {
        return this.clientHandler;
    }
}
