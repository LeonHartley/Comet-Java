package com.cometsrv.network;

import com.cometsrv.boot.Comet;
import com.cometsrv.config.CometSettings;
import com.cometsrv.network.http.ManagementServer;
import com.cometsrv.network.messages.MessageHandler;
import com.cometsrv.network.sessions.Session;
import com.cometsrv.network.sessions.SessionManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NetworkEngine {
    public static final AttributeKey<Session> SESSION_ATTRIBUTE_KEY = AttributeKey.valueOf("Session.attr");
    public static final AttributeKey<UUID> UNIQUE_ID_KEY = AttributeKey.valueOf("SessionKey.attr");

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private SessionManager sessions;
    private MessageHandler messageHandler;
    private ManagementServer managementServer;

    public String ip;
    public int port;

    private Channel listenChannel;

    private static Logger log = Logger.getLogger(NetworkEngine.class.getName());

    public NetworkEngine(String ip, int port) {
        this.sessions = new SessionManager();
        this.messageHandler = new MessageHandler();

        if(CometSettings.httpEnabled)
            this.managementServer = new ManagementServer();

        // TODO: learn more about this
        //ExecutionHandler execution = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(10000, 1048576, 1073741824, 100, TimeUnit.MILLISECONDS, Executors.defaultThreadFactory()));

        // TODO: find out the best way to handle this - cachedthreadpool or orderedmemoryawarethreadpool
        //NioServerSocketChannelFactory channels = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        /*NioServerSocketChannelFactory channels = new NioServerSocketChannelFactory(execution.getExecutor(), execution.getExecutor());
        ServerBootstrap bootstrap = new ServerBootstrap(channels);*/

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(this.bossGroup, this.workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NetworkChannelInitializer());

        this.ip = ip;
        this.port = port;

        /*ChannelPipeline pipeline = bootstrap.getPipeline();

        pipeline.addLast("encoder", new MessageEncoder());
        pipeline.addLast("decoder", new MessageDecoder());
        pipeline.addLast("handler", new ClientHandler());*/

        try {
            this.listenChannel = bootstrap.bind(new InetSocketAddress(ip, port)).channel();
        } catch(Exception e) {
            Comet.exit("Failed to initialize sockets on address: " + ip + ":" + port);
            return;
        }

        log.info("CometServer listening on port: " + port);
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
