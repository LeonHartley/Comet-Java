package com.cometsrv.network.messages.incoming.navigator;

import com.cometsrv.game.GameEngine;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.navigator.FeaturedRoomsMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class FeaturedRoomsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        client.send(FeaturedRoomsMessageComposer.compose(GameEngine.getNavigator().getFeaturedRooms()));
    }
}
