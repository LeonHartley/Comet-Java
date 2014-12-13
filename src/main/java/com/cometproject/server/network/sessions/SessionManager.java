package com.cometproject.server.network.sessions;

import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.network.messages.types.Composer;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import java.util.Map;
import java.util.Set;


public final class SessionManager {
    private final FastMap<Integer, Session> sessions = new FastMap<Integer, Session>().shared();

    private final ChannelGroup channelGroup = new DefaultChannelGroup(SessionManager.class.getName());

    public boolean add(Channel channel) {
        Session session = new Session(channel);

        this.channelGroup.add(channel);
        channel.setAttachment(session);
        return (this.sessions.putIfAbsent(channel.getId(), session) == null);
    }

    public boolean remove(Channel channel) {
        if (this.sessions.containsKey(channel.getId())) {
            this.channelGroup.remove(channel);
            this.sessions.remove(channel.getId());
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

    public void broadcast(Composer msg) {
        this.getChannelGroup().write(msg);
//
//        for (Session client : sessions.values()) {
//            client.getChannel().write(msg);
//        }
    }

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }
}