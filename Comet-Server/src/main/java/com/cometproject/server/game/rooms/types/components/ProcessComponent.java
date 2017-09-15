package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.processing.AbstractRoomProcess;

public class ProcessComponent extends AbstractRoomProcess {

    public ProcessComponent(Room room) {
        super(room, 500);
    }

    @Override
    protected boolean needsProcessing(RoomEntity entity) {
        return true;
    }
}