package com.cometsrv.network;

import com.cometsrv.boot.Comet;
import com.cometsrv.config.CometSettings;
import com.cometsrv.network.clients.ClientHandler;
import com.cometsrv.network.codec.MessageDecoder;
import com.cometsrv.network.codec.MessageEncoder;
import com.cometsrv.network.http.ManagementServer;
import com.cometsrv.network.messages.MessageHandler;
import com.cometsrv.network.sessions.SessionManager;
import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

        if(CometSettings.httpEnabled)
            this.managementServer = new ManagementServer();

        // TODO: learn more about this
        ExecutionHandler execution = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(10000, 1048576, 1073741824, 100, TimeUnit.MILLISECONDS, Executors.defaultThreadFactory()));

        // TODO: find out the best way to handle this - cachedthreadpool or orderedmemoryawarethreadpool
        //NioServerSocketChannelFactory channels = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        NioServerSocketChannelFactory channels = new NioServerSocketChannelFactory(execution.getExecutor(), execution.getExecutor());
        ServerBootstrap bootstrap = new ServerBootstrap(channels);

        this.ip = ip;
        this.port = port;

        ChannelPipeline pipeline = bootstrap.getPipeline();

        pipeline.addLast("encoder", new MessageEncoder());
        pipeline.addLast("decoder", new MessageDecoder());
        pipeline.addLast("handler", new ClientHandler());

        try {
            bootstrap.bind(new InetSocketAddress(ip, port));
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
