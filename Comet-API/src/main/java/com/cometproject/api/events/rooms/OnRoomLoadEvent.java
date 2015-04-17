package com.cometproject.api.events.rooms;

import com.cometproject.api.events.Event;
import com.cometproject.api.game.rooms.Room;

public class OnRoomLoadEvent extends Event {
    private Room room;

    public OnRoomLoadEvent(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return this.room;
    }
}
