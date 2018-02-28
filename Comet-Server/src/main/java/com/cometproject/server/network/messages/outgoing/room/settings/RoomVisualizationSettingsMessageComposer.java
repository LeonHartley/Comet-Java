package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class RoomVisualizationSettingsMessageComposer extends MessageComposer {
    private final boolean hideWall;
    private final int wallThick;
    private final int floorThick;

    public RoomVisualizationSettingsMessageComposer(boolean hideWall, int wallThick, int floorThick) {
        this.hideWall = hideWall;
        this.wallThick = wallThick;
        this.floorThick = floorThick;
    }

    @Override
    public short getId() {
        return Composers.RoomVisualizationSettingsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeBoolean(hideWall);
        msg.writeInt(wallThick);
        msg.writeInt(floorThick);
    }
}
