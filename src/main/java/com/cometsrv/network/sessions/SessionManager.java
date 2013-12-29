package com.cometsrv.network.sessions;

import javolution.util.FastMap;
import org.jboss.netty.channel.Channel;

import java.util.Map;

public class SessionManager {
    private FastMap<Integer, Session> sessions;

    public SessionManager() {
        this.sessions = new FastMap<>();
    }

    public boolean add(Channel channel) {
        Session session = new Session(channel);
        channel.setAttachment(session);

        return this.getSessions().putIfAbsent(channel.getId(), session) == null;
    }

    public boolean remove(Channel channel) {
        if(this.getSessions().containsKey(channel.getId())) {
            this.getSessions().remove(channel.getId());
            return true;
        }

        return false;
    }

    public boolean isPlayerLogged(int id) {
        for(Map.Entry<Integer, Session> session : this.getSessions().entrySet()) {
            if(session.getValue().getPlayer() != null) {
                if(session.getValue().getPlayer().getId() == id) {
                    return true;
                }
            }
        }

        return false;
    }

    public void disconnectByPlayerId(int id) {
        for(Map.Entry<Integer, Session> session : this.getSessions().entrySet()) {
            if(session.getValue().getPlayer() != null) {
                if(session.getValue().getPlayer().getId() == id) {
                    session.getValue().disconnect();
                }
            }
        }
    }

    public Session getByPlayerId(int id) {
        for(Map.Entry<Integer, Session> session : this.getSessions().entrySet()) {
            if(session.getValue().getPlayer() != null) {
                if(session.getValue().getPlayer().getId() == id) {
                    return session.getValue();
                }
            }
        }

        return null;
    }

    public Session getByPlayerUsername(String username) {
        for(Map.Entry<Integer, Session> session : this.getSessions().entrySet()) {
            if(session.getValue().getPlayer() != null && session.getValue().getPlayer().getData() != null) {
                if(session.getValue().getPlayer().getData().getUsername().equals(username)) {
                    return session.getValue();
                }
            }
        }

        return null;
    }

    public int getUsersOnlineCount() {
        int i = 0;

        for(Map.Entry<Integer, Session> session : this.getSessions().entrySet()) {
            if(session.getValue().getPlayer() != null) {
                i++;
            }
        }

        return i;
    }

    public synchronized FastMap<Integer, Session> getSessions() {
        return this.sessions;
    }
}
