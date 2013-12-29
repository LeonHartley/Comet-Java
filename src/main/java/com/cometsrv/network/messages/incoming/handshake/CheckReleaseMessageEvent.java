package com.cometsrv.network.messages.incoming.handshake;

import com.cometsrv.game.GameEngine;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class CheckReleaseMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        GameEngine.getLogger().debug("Client running on release: " + msg.readString());
    }
}
