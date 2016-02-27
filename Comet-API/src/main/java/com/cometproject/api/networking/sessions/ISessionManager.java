package com.cometproject.api.networking.sessions;

import com.cometproject.api.networking.messages.IMessageComposer;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.Set;

public interface ISessionManager {
    boolean disconnectByPlayerId(int id);

    BaseSession getByPlayerId(int id);

    Set<BaseSession> getByPlayerPermission(String permission);

    BaseSession getByPlayerUsername(String username);

    int getUsersOnlineCount();

    Map<Integer, BaseSession> getSessions();

    void broadcast(IMessageComposer msg);

    void broadcastToModerators(IMessageComposer messageComposer);

    void parseCommand(String[] message, ChannelHandlerContext ctx);
}
