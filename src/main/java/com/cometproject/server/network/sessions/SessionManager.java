package com.cometproject.server.network.sessions;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.NetworkEngine;
import com.cometproject.server.network.messages.types.Composer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import javolution.util.FastMap;

import java.util.Map;

public class SessionManager {

    private FastMap<ChannelId, Session> sessions = new FastMap<ChannelId, Session>().atomic();

    public boolean add(Channel channel) {
        Session session = new Session(channel);
        channel.attr(NetworkEngine.SESSION_ATTRIBUTE_KEY).set(session);

        return (this.sessions.putIfAbsent(channel.id(), session) == null);
    }

    public boolean remove(Channel channel) {
        if (this.sessions.containsKey(channel.id())) {
            this.sessions.remove(channel.id());
            return true;
        }

        return false;
    }

    public boolean isPlayerLogged(int id) {
        for (Map.Entry<ChannelId, Session> sessions : this.sessions.entrySet()) {
            Session session = sessions.getValue();

            if (session.getPlayer() != null) {
                if (session.getPlayer().getId() == id) {
                    return true;
                }
            }
        }

        return false;
    }

    public void disconnectByPlayerId(int id) {
        for (Map.Entry<ChannelId, Session> session : this.sessions.entrySet()) {
            if (session.getValue().getPlayer() != null) {
                if (session.getValue().getPlayer().getId() == id) {
                    session.getValue().disconnect();
                }
            }
        }
    }

    public Session getByPlayerId(int id) {
        for (Map.Entry<ChannelId, Session> session : this.sessions.entrySet()) {
            if (session.getValue().getPlayer() != null) {
                if (session.getValue().getPlayer().getId() == id) {
                    return session.getValue();
                }
            }
        }

        return null;
    }

    public Session getByPlayerUsername(String username) {
        for (Map.Entry<ChannelId, Session> session : this.sessions.entrySet()) {
            if (session.getValue().getPlayer() != null && session.getValue().getPlayer().getData() != null) {
                if (session.getValue().getPlayer().getData().getUsername().toLowerCase().equals(username.toLowerCase())) {
                    return session.getValue();
                }
            }
        }

        return null;
    }

    public int getUsersOnlineCount() {
        int i = 0;

        for (Map.Entry<ChannelId, Session> session : this.sessions.entrySet()) {
            if (session.getValue().getPlayer() != null) {
                i++;
            }
        }

        return i;
    }

    public Map<ChannelId, Session> getSessions() {
        return this.sessions.unmodifiable();
    }

    public void broadcast(Composer msg) {
        for (Session client : sessions.values()) {
            client.getChannel().writeAndFlush(msg.get().duplicate().retain());
        }
    }
}
