package com.cometproject.server.network.sessions;

import com.cometproject.server.network.NetworkEngine;
import javolution.util.FastMap;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private FastMap<UUID, Session> sessions = new FastMap<UUID, Session>().shared();

    public boolean add(Channel channel) {
        UUID uniqueId = UUID.randomUUID();

        Session session = new Session(channel);
        channel.attr(NetworkEngine.SESSION_ATTRIBUTE_KEY).set(session);
        channel.attr(NetworkEngine.UNIQUE_ID_KEY).set(uniqueId);

        return (this.sessions.putIfAbsent(uniqueId, session) == null);
    }

    public boolean remove(Channel channel) {
        if(this.sessions.containsKey(channel.attr(NetworkEngine.UNIQUE_ID_KEY).get())) {
            this.sessions.remove(channel.attr(NetworkEngine.UNIQUE_ID_KEY).get());
            return true;
        }

        return false;
    }

    public boolean isPlayerLogged(int id) {
        for(Map.Entry<UUID, Session> sessions : this.sessions.entrySet()) {
            Session session = sessions.getValue();

            if(session.getPlayer() != null) {
                if(session.getPlayer().getId() == id) {
                    return true;
                }
            }
        }

        return false;
    }

    public void disconnectByPlayerId(int id) {
        for(Map.Entry<UUID, Session> session : this.sessions.entrySet()) {
            if(session.getValue().getPlayer() != null) {
                if(session.getValue().getPlayer().getId() == id) {
                    session.getValue().disconnect();
                }
            }
        }
    }

    public Session getByPlayerId(int id) {
        for(Map.Entry<UUID, Session> session : this.sessions.entrySet()) {
            if(session.getValue().getPlayer() != null) {
                if(session.getValue().getPlayer().getId() == id) {
                    return session.getValue();
                }
            }
        }

        return null;
    }

    public Session getByPlayerUsername(String username) {
        for(Map.Entry<UUID, Session> session : this.sessions.entrySet()) {
            if(session.getValue().getPlayer() != null && session.getValue().getPlayer().getData() != null) {
                if(session.getValue().getPlayer().getData().getUsername().toLowerCase().equals(username.toLowerCase())) {
                    return session.getValue();
                }
            }
        }

        return null;
    }

    public int getUsersOnlineCount() {
        int i = 0;

        for(Map.Entry<UUID, Session> session : this.sessions.entrySet()) {
            if(session.getValue().getPlayer() != null) {
                i++;
            }
        }

        return i;
    }

    public Map<UUID, Session> getSessions() {
        return this.sessions.unmodifiable();
    }
}
