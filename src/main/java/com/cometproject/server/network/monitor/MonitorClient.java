package com.cometproject.server.network.monitor;

public class MonitorClient {
    /*public final String MONITOR_HOST = "monitor.cometproject.com";
    public final int MONITOR_PORT = 1337;

    private Logger log = Logger.getLogger(MonitorClient.class.getName());

    public MonitorClient() {
        Thread monitorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup group = new NioEventLoopGroup();

                try {
                    Bootstrap bootstrap = new Bootstrap();

                    bootstrap.group(group)
                            .channel(NioSocketChannel.class)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    socketChannel.pipeline().addLast(new MonitorClientHandler());
                                }
                            });

                    ChannelFuture future = bootstrap.connect(MONITOR_HOST, MONITOR_PORT).sync();

                    future.channel().closeFuture().sync();
                } catch (Exception e) {
                    if (e instanceof ConnectException) {
                        log.warn("Could not connect to monitor server. " + e.getMessage());
                        return;
                    }

                    log.error("Error while initializing monitor client", e);
                } finally {
                    group.shutdownGracefully();
                }
            }
        });

        monitorThread.start();
    }*/
}
