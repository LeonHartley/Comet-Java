package com.cometproject.network.messages.incoming.room.item;

import com.cometproject.game.rooms.types.Room;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class UseMoodlightMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        Room room = client.getPlayer().getEntity().getRoom();

        if(room == null) {
            return;
        }


    }
}
