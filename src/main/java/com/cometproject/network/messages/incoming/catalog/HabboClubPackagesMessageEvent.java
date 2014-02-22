package com.cometproject.network.messages.incoming.catalog;

import com.cometproject.game.GameEngine;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.catalog.ClubMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class HabboClubPackagesMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(ClubMessageComposer.compose(GameEngine.getCatalog().getClubOffers()));
    }
}
