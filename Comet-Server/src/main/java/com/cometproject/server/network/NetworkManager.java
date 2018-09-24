package com.cometproject.server.network;

import com.cometproject.api.config.CometSettings;
import com.cometproject.api.config.Configuration;
import com.cometproject.api.messaging.console.ConsoleCommandRequest;
import com.cometproject.api.messaging.exec.ExecCommandRequest;
import com.cometproject.api.messaging.exec.ExecCommandResponse;
import com.cometproject.api.messaging.status.StatusRequest;
import com.cometproject.api.messaging.status.StatusResponse;
import com.cometproject.networking.api.INetworkingServer;
import com.cometproject.networking.api.INetworkingServerFactory;
import com.cometproject.networking.api.NetworkingContext;
import com.cometproject.networking.api.config.NetworkingServerConfig;
import com.cometproject.networking.api.sessions.INetSessionFactory;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.boot.utils.ConsoleCommands;
import com.cometproject.server.network.messages.GameMessageHandler;
import com.cometproject.server.network.messages.MessageHandler;
import com.cometproject.server.network.monitor.MonitorClient;
import com.cometproject.server.network.sessions.SessionManager;
import com.cometproject.server.network.sessions.net.NetSessionFactory;
import com.cometproject.server.network.ws.WsMessageHandler;
import com.cometproject.server.protocol.security.exchange.RSA;
import com.google.common.collect.Sets;
import io.coerce.commons.config.CoerceConfiguration;
import io.coerce.services.messaging.client.MessagingClient;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
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
    private MonitorClient monitorClient;
    private MessagingClient messagingClient;

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
            this.messagingClient = MessagingClient.create("com.cometproject:instance/" + Comet.instanceId + "/" +
                            "" + CometSettings.hotelName.replace(" ", "-").toLowerCase(),
                    new CoerceConfiguration("configuration/Coerce.json"));

            this.messagingClient.observe(ConsoleCommandRequest.class, (consoleCommandRequest -> {
                ConsoleCommands.handleCommand(consoleCommandRequest.getCommand());
            }));

            this.messagingClient.observe(ExecCommandRequest.class, (execRequest -> {
                final String command = execRequest.getCommand();

                try {
                    Process process = Runtime.getRuntime().exec(command);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            process.getInputStream()));

                    StringBuilder commandOutput = new StringBuilder();
                    String buffer;

                    while ((buffer = reader.readLine()) != null) {
                        commandOutput.append(buffer).append("\n");
                    }

                    this.messagingClient.sendResponse(execRequest.getMessageId(), execRequest.getSender(),
                            new ExecCommandResponse(commandOutput.toString()));
                } catch (IOException e) {
                    this.messagingClient.sendResponse(execRequest.getMessageId(), execRequest.getSender(),
                            new ExecCommandResponse("Exception: " + e));
                }
            }));

            this.messagingClient.observe(StatusRequest.class, (statusRequest -> {
                messagingClient.sendResponse(statusRequest.getMessageId(), statusRequest.getSender(),
                        new StatusResponse(Comet.getStats(), Comet.getBuild()));
            }));

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

//            WebSocketServer.getInstance().initialize();

            final InetAddress address = InetAddress.getLocalHost();
//            final InetAddress address = Address.getByName("master.cometproject.com");

            this.messagingClient.connect(address.getHostAddress(), 6500, (client) -> {
                // Create logging appender!
                // Initialise with the master service.

            });
        } catch (Exception e) {
            System.out.println("Failed to initialise NetworkManager");
            System.exit(0);
            return;
        }

        this.rsa.init();

        InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());

        System.setProperty("io.netty.leakDetectionLevel", "disabled");
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

//        final NettyNetworkingServerFactory serverFactory = new NettyNetworkingServerFactory(Configuration.currentConfig());

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

    public MonitorClient getMonitorClient() {
        return monitorClient;
    }

    public RSA getRSA() {
        return rsa;
    }

    public int getServerPort() {
        return serverPort;
    }

    public MessagingClient getMessagingClient() {
        return this.messagingClient;
    }
}
