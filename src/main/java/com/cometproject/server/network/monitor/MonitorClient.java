//package com.cometproject.server.network.monitor;
//
//import com.cometproject.server.boot.Comet;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.*;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import org.apache.log4j.Logger;
//
//import java.util.concurrent.TimeUnit;
//
//public class MonitorClient {
//    public final String MONITOR_HOST = "monitor.cometproject.com";
//    public final int MONITOR_PORT = 13337;
//
//    public MonitorClient(EventLoopGroup loopGroup) {
//        createBootstrap(new Bootstrap(), loopGroup);
//    }
//
//    public Bootstrap createBootstrap(Bootstrap bootstrap, EventLoopGroup eventLoop) {
//        if (bootstrap != null) {
//            final MonitorClientHandler handler = new MonitorClientHandler();
//
//            bootstrap.group(eventLoop);
//            bootstrap.channel(NioSocketChannel.class);
//
//            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
//            bootstrap.option(ChannelOption.TCP_NODELAY, true);
//
//            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                protected void initChannel(SocketChannel socketChannel) throws Exception {
//                    socketChannel.pipeline().addLast(handler);
//                }
//            });
//
//            bootstrap.remoteAddress(Comet.isDebugging ? "localhost" : MONITOR_HOST, MONITOR_PORT);
//            bootstrap.connect().addListener(new ConnectionListener(this));
//        }
//        return bootstrap;
//    }
//
//    public class ConnectionListener implements ChannelFutureListener {
//        private MonitorClient client;
//
//        public ConnectionListener(MonitorClient client) {
//            this.client = client;
//        }
//
//        @Override
//        public void operationComplete(ChannelFuture channelFuture) throws Exception {
//            if (!channelFuture.isSuccess()) {
//                MonitorClientHandler.isConnected = false;
//
//                final EventLoop loop = channelFuture.channel().eventLoop();
//                loop.schedule(() -> client.createBootstrap(new Bootstrap(), loop), 1L, TimeUnit.SECONDS);
//            }
//        }
//    }
//}
