package com.cometproject.api.networking.sessions;

import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.networking.messages.IMessageComposer;

public interface ISession {
    IPlayer getPlayer();

    void disconnect();

    void send(IMessageComposer messageComposer);

    String getIpAddress();
}
