package com.cometproject.server.network.messages.incoming.handshake;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class CheckReleaseMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        GameEngine.getLogger().debug("Client running on release: " + msg.readString().split(" ")[0]);
    }
}
