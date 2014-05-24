package com.cometproject.server.network;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.http.ManagementServer;
import com.cometproject.server.network.messages.MessageHandler;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.network.sessions.SessionManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class NetworkEngine {
    private SessionManager sessions;
    private MessageHandler messageHandler;
    private ManagementServer managementServer;

    private final List<Channel> listeners = new ArrayList<>();

    public static final AttributeKey<Session> SESSION_ATTR = AttributeKey.valueOf("Session.attr");
    public static final AttributeKey<Integer> CHANNEL_ID = AttributeKey.valueOf("ChannelId.attr");

    private static Logger log = Logger.getLogger(NetworkEngine.class.getName());

    public static boolean supportsEpoll() {
        String OS = System.getProperty("os.name").toLowerCase();
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    public NetworkEngine(String ip, String ports) {
        this.sessions = new SessionManager();
        this.messageHandler = new MessageHandler();

        if (CometSettings.httpEnabled) { this.managementServer = new ManagementServer(); }

        int poolSize = Integer.parseInt(Comet.getServer().getConfig().get("comet.threading.pool.size"));
        if (poolSize < 1) { poolSize = (Runtime.getRuntime().availableProcessors() * 2); }

        if (supportsEpoll()) {
            log.info("OS has Epoll support, using high performance Epoll selectors");
        } else {
            log.info("Epoll is not supported, using Nio selectors");
        }

        EventLoopGroup bossGroup = !supportsEpoll() ? new NioEventLoopGroup() : new EpollEventLoopGroup();
        EventLoopGroup workerGroup = !supportsEpoll() ? new NioEventLoopGroup() : new EpollEventLoopGroup();

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);

        if (ResourceLeakDetector.isEnabled()) {
            log.warn("Resource Leak detection is enabled");
        }

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(!supportsEpoll() ? NioServerSocketChannel.class : EpollServerSocketChannel.class)
                .childHandler(new NetworkChannelInitializer(poolSize))
                .option(ChannelOption.SO_BACKLOG, 500)
                .option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 32 * 1024)
                .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 64 * 1024)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

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
            listeners.add(bootstrap.bind(new InetSocketAddress(ip, port)).channel());
            log.info("CometServer listening on port: " + port);
        } catch (Exception e ) {
            Comet.exit("Failed to initialize sockets on address: " + ip + ":" + port);
        }
    }

    public void stopListeners() {
        for (Channel c : this.listeners) {
            c.close();
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
}
