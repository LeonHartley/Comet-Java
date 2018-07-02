package com.cometproject.server.network;

import com.cometproject.api.config.CometSettings;
import com.cometproject.networking.api.sessions.INetSessionFactory;
import com.cometproject.server.network.clients.ClientHandler;
import com.cometproject.server.network.sessions.SessionAccessLog;
import com.cometproject.server.protocol.codec.MessageDecoder;
import com.cometproject.server.protocol.codec.MessageEncoder;
import com.cometproject.server.protocol.codec.XMLPolicyDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NetworkChannelInitializer extends ChannelInitializer<SocketChannel> {
    private static final Logger log = Logger.getLogger(NetworkChannelInitializer.class);

    private final EventExecutorGroup executor;
    private final INetSessionFactory sessionFactory;

    private final ClientHandler clientHandler;

    public NetworkChannelInitializer(EventExecutorGroup executorGroup, INetSessionFactory sessionFactory) {
        this.executor = executorGroup;
        this.sessionFactory = sessionFactory;

        this.clientHandler = new ClientHandler(sessionFactory);
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        // Check if we should register the client or disconnect it
        final String ipAddress = ch.remoteAddress().getAddress().getHostAddress();

        final Map<String, SessionAccessLog> accessLog = NetworkManager.getInstance().getSessions().getAccessLog();

        if (CometSettings.maxConnectionsBlockSuspicious && NetworkManager.getInstance().getSessions().getAccessLog().containsKey(ipAddress)) {
            final SessionAccessLog sessionAccessLog = accessLog.get(ipAddress);

            if (sessionAccessLog.isSuspicious()) {
                ch.disconnect();
                log.warn(String.format("Client denied, address: %s", ipAddress));
                return;
            }

            sessionAccessLog.incrementCounter();
        } else {
            accessLog.put(ipAddress, new SessionAccessLog());
        }

        ch.config().setTrafficClass(0x18);

        ch.pipeline()
                .addLast("xmlDecoder", new XMLPolicyDecoder())
                .addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8))
                .addLast("messageDecoder", new MessageDecoder())
                .addLast("messageEncoder", new MessageEncoder());

        if (NetworkManager.IDLE_TIMER_ENABLED) {
            ch.pipeline().addLast("idleHandler",
                    new IdleStateHandler(
                            NetworkManager.IDLE_TIMER_READER_TIME,
                            NetworkManager.IDLE_TIMER_WRITER_TIME,
                            NetworkManager.IDLE_TIMER_ALL_TIME,
                            TimeUnit.SECONDS));
        }

        ch.pipeline().addLast(this.executor, "clientHandler", this.clientHandler);
    }
}
