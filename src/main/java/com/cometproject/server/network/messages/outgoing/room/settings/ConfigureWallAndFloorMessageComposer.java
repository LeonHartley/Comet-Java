package com.cometproject.server.network.messages.outgoing.room.settings;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class ConfigureWallAndFloorMessageComposer {
    public static Composer compose(boolean hideWall, int wallThick, int floorThick) {
        Composer msg = new Composer(Composers.RoomFloorWallLevelsMessageComposer);

        msg.writeBoolean(hideWall);
        msg.writeInt(wallThick);
        msg.writeInt(floorThick);

        return msg;
    }
}
