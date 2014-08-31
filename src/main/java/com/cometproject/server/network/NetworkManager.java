package com.cometproject.server.network;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.http.ManagementServer;
import com.cometproject.server.network.messages.MessageHandler;
import com.cometproject.server.network.monitor.MonitorClient;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.sessions.SessionManager;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultMessageSizeEstimator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

public class NetworkManager {
    private SessionManager sessions;
    private MessageHandler messageHandler;
    private ManagementServer managementServer;
    private MonitorClient monitorClient;

    public static int serverPort = 0;

    public static final AttributeKey<Session> SESSION_ATTR = AttributeKey.valueOf("Session.attr");
    public static final AttributeKey<Integer> CHANNEL_ID = AttributeKey.valueOf("ChannelId.attr");

    private final PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;

    private static Logger log = Logger.getLogger(NetworkManager.class.getName());

    public NetworkManager(String ip, String ports) {
        this.sessions = new SessionManager();
        this.messageHandler = new MessageHandler();

//        InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());

        if (CometSettings.httpEnabled) {
            this.managementServer = new ManagementServer();
        }

        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("io.netty.selectorAutoRebuildThreshold", "0");

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        EventLoopGroup acceptGroup = new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty NIO Accept Thread #%1$d").build());
        EventLoopGroup ioGroup = new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty NIO IO Thread #%1$d").build());

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(acceptGroup, ioGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NetworkChannelInitializer(0))
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 32 * 1024)
                .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 64 * 1024)
                .option(ChannelOption.ALLOCATOR, allocator)
                .option(ChannelOption.MESSAGE_SIZE_ESTIMATOR, new DefaultMessageSizeEstimator(256))
                .option(ChannelOption.TCP_NODELAY, true);

        if (ports.contains(",")) {
            for (String s : ports.split(",")) {
                this.bind(bootstrap, ip, Integer.parseInt(s));
            }
        } else {
            this.bind(bootstrap, ip, Integer.parseInt(ports));
        }

        this.monitorClient = new MonitorClient(acceptGroup);
    }

    private void bind(ServerBootstrap bootstrap, String ip, int port) {
        try {
            NetworkManager.serverPort = port;

            bootstrap.bind(new InetSocketAddress(ip, port));
            log.info("CometServer listening on port: " + port);
        } catch (Exception e) {
            Comet.exit("Failed to initialize sockets on address: " + ip + ":" + port);
        }
    }

    public SessionManager getSessions() {
        return this.sessions;
    }

    public ManagementServer getManagement() {
        return this.managementServer;
    }

    public MessageHandler getMessages() {
        return this.messageHandler;
    }

    public PooledByteBufAllocator getAllocator() {
        return allocator;
    }

    public MonitorClient getMonitorClient() {
        return monitorClient;
    }
}
