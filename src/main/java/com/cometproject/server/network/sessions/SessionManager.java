package com.cometproject.server.network.sessions;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.network.NetworkEngine;
import com.cometproject.server.network.messages.types.Composer;
import io.netty.channel.Channel;
import javolution.util.FastMap;
import javolution.util.FastSet;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public final class SessionManager {

    private final FastMap<Integer, Session> sessions = new FastMap<Integer, Session>().shared();
    private final FastMap<Integer, Integer> playerIdToSessionId = new FastMap<Integer, Integer>().shared();

    private final AtomicInteger idGenerator = new AtomicInteger();

    public boolean add(Channel channel) {
        Session session = new Session(channel);

        int uniqueId = idGenerator.incrementAndGet();

        channel.attr(NetworkEngine.SESSION_ATTR).set(session);
        channel.attr(NetworkEngine.CHANNEL_ID).set(uniqueId);

        return (this.sessions.putIfAbsent(uniqueId, session) == null);
    }

    public boolean remove(Channel channel) {
        int channelId = channel.attr(NetworkEngine.CHANNEL_ID).get();

        if (this.sessions.containsKey(channelId)) {
            this.sessions.remove(channelId);
            return true;
        }

        return false;
    }

    public boolean disconnectByPlayerId(int id) {
        if(!this.playerIdToSessionId.containsKey(id)) {
            return false;
        }

        int sessionId = playerIdToSessionId.get(id);
        Session session = sessions.get(sessionId);

        if(session != null) {
            session.disconnect();
            return true;
        }

        return false;
    }

    public Session getByPlayerId(int id) {
         if(this.playerIdToSessionId.containsKey(id)) {
            int sessionId = this.playerIdToSessionId.get(id);

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

        if(playerId == -1)
            return null;

        int sessionId = CometManager.getPlayers().getSessionIdByPlayerId(playerId);

        if(sessionId == -1)
            return null;

        if(this.sessions.containsKey(sessionId))
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
                client.getChannel().writeAndFlush(msg.duplicate());
            }
        } finally {
            msg.get().release();
        }
    }
}
