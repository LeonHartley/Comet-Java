package com.cometproject.server.network.sessions;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.types.Composer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import javolution.util.FastMap;
import javolution.util.FastSet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public final class SessionManager {
    private final FastMap<Integer, Session> sessions = new FastMap<Integer, Session>().shared();
    private final AtomicInteger idGenerator = new AtomicInteger();

    public boolean add(ChannelHandlerContext ctx) {
        Session session = new Session(ctx);

        int uniqueId = idGenerator.incrementAndGet();

        ctx.attr(NetworkManager.SESSION_ATTR).set(session);
        ctx.attr(NetworkManager.CHANNEL_ID).set(uniqueId);

        return (this.sessions.putIfAbsent(uniqueId, session) == null);
    }

    public boolean remove(Channel channel) {
        int channelId = channel.attr(NetworkManager.CHANNEL_ID).get();

        if (this.sessions.containsKey(channelId)) {
            this.sessions.remove(channelId);
            return true;
        }

        return false;
    }

    public boolean disconnectByPlayerId(int id) {
        if (CometManager.getPlayers().getSessionIdByPlayerId(id) == -1) {
            return false;
        }

        int sessionId = CometManager.getPlayers().getSessionIdByPlayerId(id);
        Session session = sessions.get(sessionId);

        if (session != null) {
            session.disconnect();
            return true;
        }

        return false;
    }

    public Session getByPlayerId(int id) {
        if (CometManager.getPlayers().getSessionIdByPlayerId(id) != -1) {
            int sessionId = CometManager.getPlayers().getSessionIdByPlayerId(id);

            return sessions.get(sessionId);
        }

        return null;
    }

    public Set<Session> getByPlayerPermission(String permission) {
        // TODO: Optimize this
        Set<Session> sessions = new FastSet<>();

        int rank = CometManager.getPermissions().getPermissions().get(permission).getRank();

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
        int playerId = CometManager.getPlayers().getPlayerIdByUsername(username);

        if (playerId == -1)
            return null;

        int sessionId = CometManager.getPlayers().getSessionIdByPlayerId(playerId);

        if (sessionId == -1)
            return null;

        if (this.sessions.containsKey(sessionId))
            return this.sessions.get(sessionId);

        return null;
    }

    public int getUsersOnlineCount() {
        return CometManager.getPlayers().size();
    }

    public Map<Integer, Session> getSessions() {
        return this.sessions.unmodifiable();
    }

    public void broadcast(Composer msg) {
        try {
            for (Session client : sessions.values()) {
                client.getChannel().writeAndFlush(msg.duplicate().retain());
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
