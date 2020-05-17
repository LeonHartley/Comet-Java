package com.cometproject.api.networking.sessions;

import com.cometproject.api.networking.messages.IMessageComposer;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.Set;

public interface ISessionManager {
    ISession getByPlayerId(int id);

    ISession getByPlayerUsername(String username);

    int getUsersOnlineCount();

    Map<Integer, ISession> getSessions();

    void broadcast(IMessageComposer msg);

    void broadcastToModerators(IMessageComposer messageComposer);
}
