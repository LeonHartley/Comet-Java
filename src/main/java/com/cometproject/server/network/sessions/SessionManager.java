package com.cometproject.server.network.sessions;

import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.messages.composers.MessageComposer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import javolution.util.FastMap;
import javolution.util.FastSet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public final class SessionManager {
    public static final AttributeKey<Session> SESSION_ATTR = AttributeKey.valueOf("Session.attr");
    public static final AttributeKey<Integer> CHANNEL_ID_ATTR = AttributeKey.valueOf("ChannelId.attr");

    private final AtomicInteger idGenerator = new AtomicInteger();
    private final FastMap<Integer, Session> sessions = new FastMap<Integer, Session>().shared();

    private final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public boolean add(ChannelHandlerContext channel) {
        Session session = new Session(channel);

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
        Session session = sessions.get(sessionId);

        if (session != null) {
            session.disconnect();
            return true;
        }

        return false;
    }

    public Session getByPlayerId(int id) {
        if (PlayerManager.getInstance().getSessionIdByPlayerId(id) != -1) {
            int sessionId = PlayerManager.getInstance().getSessionIdByPlayerId(id);

            return sessions.get(sessionId);
        }

        return null;
    }

    public Set<Session> getByPlayerPermission(String permission) {
        // TODO: Optimize this
        Set<Session> sessions = new FastSet<>();

        int rank = PermissionsManager.getInstance().getPermissions().get(permission).getRank();

        for (Map.Entry<Integer, Session> session : this.sessions.entrySet()) {
            if (session.getValue().getPlayer() != null) {
                if (session.getValue().getPlayer().getData().getRank() >= rank) {
                    sessions.add(session.getValue());
                }
            }
        }

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
            return this.sessions.get(sessionId);

        return null;
    }

    public int getUsersOnlineCount() {
        return PlayerManager.getInstance().size();
    }

    public Map<Integer, Session> getSessions() {
        return this.sessions.unmodifiable();
    }

    public void broadcast(MessageComposer msg) {
        this.getChannelGroup().writeAndFlush(msg);
//
//        for (Session client : sessions.values()) {
//            client.getChannel().write(msg);
//        }
    }

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public void broadcastByPermission(MessageComposer messageComposer, String permission) {
        for(Session session : this.sessions.values()) {
            if(session.getPlayer() != null && session.getPlayer().getPermissions() != null && session.getPlayer().getPermissions().hasPermission(permission)) {
                session.send(messageComposer);
            }
        }
    }
}