package com.cometproject.server.network;

import com.cometproject.api.config.Configuration;
import com.cometproject.networking.api.INetworkingServer;
import com.cometproject.networking.api.INetworkingServerFactory;
import com.cometproject.networking.api.NetworkingContext;
import com.cometproject.networking.api.config.NetworkingServerConfig;
import com.cometproject.networking.api.sessions.INetSessionFactory;
import com.cometproject.server.network.messages.GameMessageHandler;
import com.cometproject.server.network.messages.MessageHandler;
import com.cometproject.server.network.sessions.SessionManager;
import com.cometproject.server.network.sessions.net.NetSessionFactory;
import com.cometproject.server.network.ws.WsMessageHandler;
import com.cometproject.server.protocol.security.exchange.RSA;
import com.google.common.collect.Sets;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Set;


public class NetworkManager {
    public static boolean IDLE_TIMER_ENABLED = Boolean.parseBoolean(Configuration.currentConfig().get("comet.network.idleTimer.enabled", "false"));
    public static int IDLE_TIMER_READER_TIME = Integer.parseInt(Configuration.currentConfig().get("comet.network.idleTimer.readerIdleTime", "30"));
    public static int IDLE_TIMER_WRITER_TIME = Integer.parseInt(Configuration.currentConfig().get("comet.network.idleTimer.writerIdleTime", "30"));
    public static int IDLE_TIMER_ALL_TIME = Integer.parseInt(Configuration.currentConfig().get("comet.network.idleTimer.allIdleTime", "30"));
    private static NetworkManager networkManagerInstance;
    private static Logger log = Logger.getLogger(NetworkManager.class.getName());
    private int serverPort;
    private SessionManager sessions;
    private MessageHandler messageHandler;
    private RSA rsa;

    public NetworkManager() {

    }

    public static NetworkManager getInstance() {
        if (networkManagerInstance == null)
            networkManagerInstance = new NetworkManager();

        return networkManagerInstance;
    }

    public void initialize(String ip, String ports) {
        this.rsa = new RSA();
        this.sessions = new SessionManager();
        this.messageHandler = new MessageHandler();

        this.serverPort = Integer.parseInt(ports.split(",")[0]);

        try {

            final ServerBootstrap sb = new ServerBootstrap();
            sb.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(30002))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(final SocketChannel ch) {
                            ch.pipeline().addLast(
                                    new HttpRequestDecoder(),
                                    new HttpObjectAggregator(65536),
                                    new HttpResponseEncoder(),
                                    new WebSocketServerProtocolHandler("/comet"), new WsMessageHandler());

                        }
                    });

            sb.bind();
        } catch (Exception e) {
            System.out.println("Failed to initialise NetworkManager");
            System.exit(0);
            return;
        }

        this.rsa.init();

        InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());

        System.setProperty("io.netty.leakDetectionLevel", "disabled");
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        final INetSessionFactory sessionFactory = new NetSessionFactory(this.sessions, new GameMessageHandler());
        final INetworkingServerFactory serverFactory = new NettyNetworkingServerFactory(Configuration.currentConfig());
        final NetworkingContext networkingContext = new NetworkingContext(serverFactory);

        NetworkingContext.setCurrentContext(networkingContext);

        final Set<Short> portSet = Sets.newHashSet();

        if (ports.contains(",")) {
            for (String port : ports.split(",")) {
                portSet.add(Short.parseShort(port));
            }
        } else {
            portSet.add(Short.parseShort(ports));
        }

        final INetworkingServer gameServer = serverFactory.createServer(new NetworkingServerConfig(ip, portSet),
                sessionFactory);

        gameServer.start();
    }

    public SessionManager getSessions() {
        return this.sessions;
    }

    public MessageHandler getMessages() {
        return this.messageHandler;
    }

    public RSA getRSA() {
        return rsa;
    }

    public int getServerPort() {
        return serverPort;
    }
}
