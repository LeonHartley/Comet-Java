package com.cometproject.server.network.sessions;

import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.api.networking.sessions.ISessionManager;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.protocol.security.exchange.DiffieHellman;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.hadoop.hdfs.util.Diff;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public final class SessionManager implements ISessionManager {
    public static final AttributeKey<Session> SESSION_ATTR = AttributeKey.valueOf("Session.attr");
    public static final AttributeKey<Integer> CHANNEL_ID_ATTR = AttributeKey.valueOf("ChannelId.attr");

    private final AtomicInteger idGenerator = new AtomicInteger();
    private final Map<Integer, ISession> sessions = new ConcurrentHashMap<>();

    private final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public boolean add(ChannelHandlerContext channel) {
        Session session = new Session(channel);

//        if(PlayerManager.getInstance().getPlayerCountByIpAddress(session.getIpAddress()) > CometSettings.maxConnectionsPerIpAddress) {
//            return false;
//        }

        this.channelGroup.add(channel.channel());
        channel.attr(CHANNEL_ID_ATTR).set(this.idGenerator.incrementAndGet());

        return (this.sessions.putIfAbsent(channel.attr(CHANNEL_ID_ATTR).get(), session) == null);
    }

    public boolean remove(ChannelHandlerContext channel) {
        if (this.sessions.containsKey(channel.attr(CHANNEL_ID_ATTR).get())) {
            this.channelGroup.remove(channel.channel());
            this.sessions.remove(channel.attr(CHANNEL_ID_ATTR).get());

            return true;
        }

        return false;
    }

    public boolean disconnectByPlayerId(int id) {
        if (PlayerManager.getInstance().getSessionIdByPlayerId(id) == -1) {
            return false;
        }

        int sessionId = PlayerManager.getInstance().getSessionIdByPlayerId(id);
        Session session = (Session) sessions.get(sessionId);

        if (session != null) {
            session.disconnect();
            return true;
        }

        return false;
    }

    public Session getByPlayerId(int id) {
        if (PlayerManager.getInstance().getSessionIdByPlayerId(id) != -1) {
            int sessionId = PlayerManager.getInstance().getSessionIdByPlayerId(id);

            return (Session) sessions.get(sessionId);
        }

        return null;
    }

    public Set<ISession> getByPlayerPermission(String permission) {
        // TODO: Optimize this
        Set<ISession> sessions = new HashSet<>();

        //so i dont forget :$
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");
        Comet.getServer().getLogger().info("FIX THIS LEON FIX THIS FIX THIS FIX THISSSSSSSSS");

//
//        int rank = PermissionsManager.getInstance().getPermissions().get(permission).getRank();
//
//        for (Map.Entry<Integer, ISession> session : this.sessions.entrySet()) {
//            if (session.getValue().getPlayer() != null) {
//                if (((Session) session.getValue()).getPlayer().getData().getRank() >= rank) {
//                    sessions.add(session.getValue());
//                }
//            }
//        }

        return sessions;
    }

    public Session getByPlayerUsername(String username) {
        int playerId = PlayerManager.getInstance().getPlayerIdByUsername(username);

        if (playerId == -1)
            return null;

        int sessionId = PlayerManager.getInstance().getSessionIdByPlayerId(playerId);

        if (sessionId == -1)
            return null;

        if (this.sessions.containsKey(sessionId))
            return (Session) this.sessions.get(sessionId);

        return null;
    }

    public int getUsersOnlineCount() {
        return PlayerManager.getInstance().size();
    }

    public Map<Integer, ISession> getSessions() {
        return this.sessions;
    }

    public void broadcast(IMessageComposer msg) {
        this.getChannelGroup().writeAndFlush(msg);
//
//        for (Session client : sessions.values()) {
//            client.getChannel().write(msg);
//        }
    }

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public void broadcastToModerators(IMessageComposer messageComposer) {
        for (ISession session : this.sessions.values()) {
            if (session.getPlayer() != null && session.getPlayer().getPermissions() != null && session.getPlayer().getPermissions().getRank().modTool()) {
                session.send(messageComposer);
            }
        }
    }
}