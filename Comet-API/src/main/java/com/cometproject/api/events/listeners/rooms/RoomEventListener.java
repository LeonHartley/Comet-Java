package com.cometproject.api.events.listeners.rooms;

import com.cometproject.api.events.rooms.OnRoomLoadEvent;

public interface RoomEventListener {
    void onRoomLoad(OnRoomLoadEvent event);
}
