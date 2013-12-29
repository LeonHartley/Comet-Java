package com.cometsrv.network.messages.outgoing.room.settings;

import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class ConfigureWallAndFloorMessageComposer {
    public static Composer compose(boolean hideWall, int wallThick, int floorThick) {
        Composer msg = new Composer(Composers.ConfigureWallAndFloorMessageComposer);

        msg.writeBoolean(hideWall);
        msg.writeInt(wallThick);
        msg.writeInt(floorThick);

        return msg;
    }
}
