package com.cometsrv.network.messages.incoming.catalog;

import com.cometsrv.game.GameEngine;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.catalog.ClubMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class HabboClubPackagesMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(ClubMessageComposer.compose(GameEngine.getCatalog().getClubOffers()));
    }
}
