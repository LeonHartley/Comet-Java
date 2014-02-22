package com.cometproject.server.network.messages.incoming.catalog;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.ClubMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class HabboClubPackagesMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(ClubMessageComposer.compose(GameEngine.getCatalog().getClubOffers()));
    }
}
