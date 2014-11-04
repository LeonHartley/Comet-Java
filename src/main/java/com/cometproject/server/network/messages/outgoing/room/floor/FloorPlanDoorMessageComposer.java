package com.cometproject.server.network.messages.outgoing.room.floor;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class FloorPlanDoorMessageComposer {
    public static Composer compose(int x, int y, int rotation) {
        Composer msg = new Composer(Composers.SetFloorPlanDoorMessageComposer);

        msg.writeInt(x);
        msg.writeInt(y);
        msg.writeInt(rotation);

        return msg;
    }
}
