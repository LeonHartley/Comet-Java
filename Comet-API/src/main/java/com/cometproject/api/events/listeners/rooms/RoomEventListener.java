package com.cometproject.api.events.listeners.rooms;

import com.cometproject.api.events.EventListener;
import com.cometproject.api.events.rooms.OnRoomLoadEvent;

public interface RoomEventListener {
    @EventListener(event = OnRoomLoadEvent.class)
    void onRoomLoad(OnRoomLoadEvent event);
}
