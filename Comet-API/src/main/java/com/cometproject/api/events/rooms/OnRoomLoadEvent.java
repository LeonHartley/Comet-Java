package com.cometproject.api.events.rooms;

import com.cometproject.api.events.Event;
import com.cometproject.api.game.rooms.IRoom;

public class OnRoomLoadEvent extends Event {
    private IRoom room;

    public OnRoomLoadEvent(IRoom room) {
        this.room = room;
    }

    public IRoom getRoom() {
        return this.room;
    }
}
