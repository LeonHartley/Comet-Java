package com.cometproject.server.network;

import com.cometproject.api.config.Configuration;
import com.cometproject.networking.api.INetworkingServer;
import com.cometproject.networking.api.INetworkingServerFactory;
import com.cometproject.networking.api.config.NetworkingServerConfig;
import com.cometproject.networking.api.sessions.INetSessionFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultMessageSizeEstimator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyNetworkingServerFactory implements INetworkingServerFactory {
    private static final String CFG_EPOLL = "comet.network.epoll";

    private static final String CFG_IO_LOOP_COUNT = "comet.network.ioGroupThreads";
    private static final String CFG_CHANNEL_LOOP_COUNT = "comet.network.channelGroupThreads";
    private static final String CFG_ACCEPT_LOOP_COUNT = "comet.network.acceptGroupThreads";
    private static final String CFG_NETWORK_BACKLOG = "comet.network.backlog";

    private final Configuration configuration;

    private EventLoopGroup ioLoopGroup;
    private EventLoopGroup channelLoopGroup;
    private EventLoopGroup acceptLoopGroup;

    private boolean epollEnabled = false;

    public NettyNetworkingServerFactory(Configuration configuration) {
        final boolean epoll = Boolean.parseBoolean((String) configuration.getOrDefault(CFG_EPOLL, "false"));
        final int ioGroupCount = Integer.parseInt((String) configuration.getOrDefault(CFG_IO_LOOP_COUNT, "4"));
        final int channelGroupCount = Integer.parseInt((String) configuration.getOrDefault(CFG_CHANNEL_LOOP_COUNT, "4"));
        final int acceptGroupCount = Integer.parseInt((String) configuration.getOrDefault(CFG_ACCEPT_LOOP_COUNT, "2"));

        this.configuration = configuration;
        this.epollEnabled = Epoll.isAvailable() && epoll;

        this.initialiseLoopGroups(Epoll.isAvailable() && epoll, ioGroupCount, channelGroupCount, acceptGroupCount);
    }

    private void initialiseLoopGroups(final boolean epoll, final int ioLoopCount, final int channelLoopCount,
                                      final int acceptGroupCount) {
        this.ioLoopGroup = epoll ? new EpollEventLoopGroup(ioLoopCount) : new NioEventLoopGroup(ioLoopCount);
        this.channelLoopGroup = epoll ? new EpollEventLoopGroup(channelLoopCount) : new NioEventLoopGroup(channelLoopCount);
        this.acceptLoopGroup = epoll ? new EpollEventLoopGroup(acceptGroupCount) : new NioEventLoopGroup(acceptGroupCount);
    }

    @Override
    public INetworkingServer createServer(NetworkingServerConfig serverConfig, INetSessionFactory sessionFactory) {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(this.acceptLoopGroup, this.ioLoopGroup)
                .channel(this.epollEnabled ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childHandler(new NetworkChannelInitializer(this.channelLoopGroup, sessionFactory))
                .option(ChannelOption.SO_BACKLOG, Integer.parseInt((String) this.configuration.getOrDefault(CFG_NETWORK_BACKLOG, "500")))
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 32 * 1024)
                .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 64 * 1024)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.MESSAGE_SIZE_ESTIMATOR, DefaultMessageSizeEstimator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 32 * 1024)
                .childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 64 * 1024);

        return new NettyNetworkingServer(serverConfig, sessionFactory, bootstrap);
    }
}
