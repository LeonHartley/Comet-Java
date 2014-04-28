package com.cometproject.server.network;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.network.http.ManagementServer;
import com.cometproject.server.network.messages.MessageHandler;
import com.cometproject.server.network.sessions.SessionManager;
import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class NetworkEngine {
    private SessionManager sessions;
    private MessageHandler messageHandler;
    private ManagementServer managementServer;

    public String ip;
    public int port;

    private static Logger log = Logger.getLogger(NetworkEngine.class.getName());

    public NetworkEngine(String ip, int port) {
        this.sessions = new SessionManager();
        this.messageHandler = new MessageHandler();

        if (CometSettings.httpEnabled)
            this.managementServer = new ManagementServer();

        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setOption("backlog", 1000);
        bootstrap.setPipelineFactory(new NetworkChannelInitializer());


        this.ip = ip;
        this.port = port;

        try {
            bootstrap.bind(new InetSocketAddress(ip, port));
        } catch (Exception e) {
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
