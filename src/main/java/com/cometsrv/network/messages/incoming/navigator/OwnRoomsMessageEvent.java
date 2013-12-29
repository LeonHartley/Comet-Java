package com.cometsrv.network.messages.incoming.navigator;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class OwnRoomsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        client.send(NavigatorFlatListMessageComposer.compose(0, 5, "", client.getPlayer().getRooms().values()));
    }
}
