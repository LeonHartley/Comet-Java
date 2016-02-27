package com.cometproject.api.networking.sessions;

import com.cometproject.api.game.players.BasePlayer;
import com.cometproject.api.networking.messages.IMessageComposer;

public interface BaseSession {
    BasePlayer getPlayer();

    void disconnect();

    BaseSession send(IMessageComposer messageComposer);

    BaseSession sendQueue(IMessageComposer messageComposer);

    String getIpAddress();
}
