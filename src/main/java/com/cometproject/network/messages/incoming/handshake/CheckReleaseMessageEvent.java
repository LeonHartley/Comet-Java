package com.cometproject.network.messages.incoming.handshake;

import com.cometproject.game.GameEngine;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class CheckReleaseMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        GameEngine.getLogger().debug("Client running on release: " + msg.readString());
    }
}
