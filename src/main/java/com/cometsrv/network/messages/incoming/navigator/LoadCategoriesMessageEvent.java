package com.cometsrv.network.messages.incoming.navigator;

import com.cometsrv.game.GameEngine;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.navigator.RoomCategoriesMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class LoadCategoriesMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(RoomCategoriesMessageComposer.compose(GameEngine.getNavigator().getCategories(), client.getPlayer().getData().getRank()));
    }
}
