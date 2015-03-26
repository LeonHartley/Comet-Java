package com.cometproject.server.network;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.MessageHandler;
import com.cometproject.server.network.sessions.SessionManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultMessageSizeEstimator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;


public class NetworkManager {
    private static NetworkManager networkManagerInstance;

    private SessionManager sessions;
    private MessageHandler messageHandler;

    private static Logger log = Logger.getLogger(NetworkManager.class.getName());

    public NetworkManager() {

    }

    public static NetworkManager getInstance() {
        if (networkManagerInstance == null)
            networkManagerInstance = new NetworkManager();

        return networkManagerInstance;
    }

    public void initialize(String ip, String ports) {
        this.sessions = new SessionManager();
        this.messageHandler = new MessageHandler();

        InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());

        System.setProperty("io.netty.leakDetectionLevel", "disabled");
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        EventLoopGroup acceptGroup;
        EventLoopGroup ioGroup;
        EventLoopGroup channelGroup;

        final boolean isEpollAvailable = Epoll.isAvailable() && Boolean.parseBoolean(Comet.getServer().getConfig().get("comet.network.epoll", "false"));
        final int threadCount = 0; // TODO: Find the best count.

        if(isEpollAvailable) {
            log.info("Epoll is available");
            acceptGroup = new EpollEventLoopGroup(threadCount);
            ioGroup = new EpollEventLoopGroup(threadCount);
            channelGroup = new EpollEventLoopGroup(threadCount);
        } else {
            log.info("Epoll is not available");
            acceptGroup = new NioEventLoopGroup(threadCount);
            ioGroup = new NioEventLoopGroup(threadCount);
            channelGroup = new NioEventLoopGroup(threadCount);
        }

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(acceptGroup, ioGroup)
                .channel(isEpollAvailable ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childHandler(new NetworkChannelInitializer(channelGroup))
                .option(ChannelOption.SO_BACKLOG, Integer.parseInt(Comet.getServer().getConfig().get("comet.network.backlog", "500")))
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 32 * 1024)
                .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 64 * 1024)
                .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.MESSAGE_SIZE_ESTIMATOR, DefaultMessageSizeEstimator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT);

        if (ports.contains(",")) {
            for (String s : ports.split(",")) {
                this.bind(bootstrap, ip, Integer.parseInt(s));
            }
        } else {
            this.bind(bootstrap, ip, Integer.parseInt(ports));
        }
    }

    private void bind(ServerBootstrap bootstrap, String ip, int port) {
        try {
            bootstrap.bind(new InetSocketAddress(ip, port)).addListener(objectFuture -> {
                if(!objectFuture.isSuccess()) {
                    Comet.exit("Failed to initialize sockets on address: " + ip + ":" + port);
                }
            });

            log.info("CometServer listening on port: " + port);
        } catch (Exception e) {
            Comet.exit("Failed to initialize sockets on address: " + ip + ":" + port);
        }
    }

    public SessionManager getSessions() {
        return this.sessions;
    }

    public MessageHandler getMessages() {
        return this.messageHandler;
    }
}
