package com.cometproject.api.networking.sessions;

import com.cometproject.api.networking.messages.IMessageComposer;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public interface ISessionService {
    ISession getByPlayerId(int id);

    ISession getByPlayerUsername(String username);

    int getUsersOnlineCount();

    Map<Integer, ISession> getSessions();

    void broadcast(IMessageComposer msg);

    void broadcastTo(Set<Integer> players, IMessageComposer messageComposer, int sender);

    void broadcastToModerators(IMessageComposer messageComposer);
}
