package com.cometproject.server.network.messages.incoming.navigator;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.navigator.NavigatorFlatListMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class SearchRoomMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String query = msg.readString();
        client.send(NavigatorFlatListMessageComposer.compose(0, 8, "", RoomManager.getInstance().getRoomByQuery(query)));
    }
}
