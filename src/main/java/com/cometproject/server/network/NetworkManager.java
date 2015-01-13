package com.cometproject.server.network;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.MessageHandler;
import com.cometproject.server.network.sessions.SessionManager;
import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.AdaptiveReceiveBufferSizePredictorFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Log4JLoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;


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

        // set the logger to our logger
        InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());

//        int poolSize = (Runtime.getRuntime().availableProcessors() * 2);
        int poolSize = 16;

        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newFixedThreadPool(poolSize),
                        Executors.newFixedThreadPool(poolSize)
                ));

        bootstrap.setOption("backlog", 100);
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);

        // better to have an receive buffer predictor
        bootstrap.setOption("receiveBufferSizePredictorFactory", new AdaptiveReceiveBufferSizePredictorFactory(128, 1024, 8192));

        //if the server is sending 1000 messages per sec, optimum write buffer water marks will
        //prevent unnecessary throttling, Check NioSocketChannelConfig doc
        bootstrap.setOption("writeBufferLowWaterMark", 32 * 1024);
        bootstrap.setOption("writeBufferHighWaterMark", 64 * 1024);

        //int channelMemory = 65536;
        //int totalMemory = (poolSize * channelMemory);
        OrderedMemoryAwareThreadPoolExecutor handlerExecutor = new OrderedMemoryAwareThreadPoolExecutor(poolSize, 1048576, 1048576);

        bootstrap.setPipelineFactory(new NetworkChannelInitializer(handlerExecutor));

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
            bootstrap.bind(new InetSocketAddress(ip, port));
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
