package com.cometproject.server.network.messages.outgoing.room.floor;

import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class TilesInUseMessageComposer {
    public static Composer compose(List<Position> tiles) {
        Composer msg = new Composer(Composers.GetFloorPlanUsedCoordsMessageComposer);

        msg.writeInt(tiles.size());

        for(Position position : tiles) {
            msg.writeInt(position.getX());
            msg.writeInt(position.getY());
        }

        return msg;
    }
}
