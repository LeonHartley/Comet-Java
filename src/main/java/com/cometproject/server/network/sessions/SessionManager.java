package com.cometproject.server.network.sessions;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.network.messages.types.Composer;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.jboss.netty.channel.Channel;

import java.util.Map;
import java.util.Set;

public class SessionManager {

    private FastMap<Integer, Session> sessions = new FastMap<Integer, Session>().atomic();
    private FastMap<Integer, Integer> playerIdToSessionId = new FastMap<Integer, Integer>().atomic();

    public boolean add(Channel channel) {
        Session session = new Session(channel);
        channel.setAttachment(session);

        return (this.sessions.putIfAbsent(channel.getId(), session) == null);
    }

    public boolean remove(Channel channel) {
        if (this.sessions.containsKey(channel.getId())) {
            this.sessions.remove(channel.getId());
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
        /*for (Map.Entry<Integer, Session> session : this.sessions.entrySet()) {
            if (session.getValue().getPlayer() != null) {
                if (session.getValue().getPlayer().getId() == id) {
                    return session.getValue();
                }
            }
        }*/

        if(this.playerIdToSessionId.containsKey(id)) {
            int sessionId = this.playerIdToSessionId.get(id);

            return sessions.get(sessionId);
        }

        return null;
    }

    public Set<Session> getByPlayerPermission(String permission) {
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
        /*for (Map.Entry<Integer, Session> session : this.sessions.entrySet()) {
            if (session.getValue().getPlayer() != null && session.getValue().getPlayer().getData() != null) {
                if (session.getValue().getPlayer().getData().getUsername().toLowerCase().equals(username.toLowerCase())) {
                    return session.getValue();
                }
            }
        }*/

        int playerId = CometManager.getPlayers().getPlayerIdByUsername(username);

        return null;
    }

    public int getUsersOnlineCount() {
        int i = 0;

        for (Map.Entry<Integer, Session> session : this.sessions.entrySet()) {
            if (session.getValue().getPlayer() != null) {
                i++;
            }
        }

        return i;
    }

    public Map<Integer, Session> getSessions() {
        return this.sessions.unmodifiable();
    }

    public void broadcast(Composer msg) {
        for (Session client : sessions.values()) {
            client.getChannel().write(msg.get());
        }
    }
}
