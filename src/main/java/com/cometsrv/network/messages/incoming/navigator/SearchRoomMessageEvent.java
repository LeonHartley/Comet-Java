package com.cometsrv.network.messages.incoming.navigator;

import com.cometsrv.game.GameEngine;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class SearchRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String query = msg.readString();
        client.send(NavigatorFlatListMessageComposer.compose(0, 8, "", GameEngine.getRooms().getRoomByQuery(query)));
    }
}
