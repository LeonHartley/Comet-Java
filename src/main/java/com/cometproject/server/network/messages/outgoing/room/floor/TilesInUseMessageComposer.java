package com.cometproject.server.network.messages.outgoing.room.floor;

import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;


public class TilesInUseMessageComposer extends MessageComposer {
    private final List<Position> tiles;

    public TilesInUseMessageComposer(final List<Position> tiles) {
        this.tiles = tiles;
    }

    @Override
    public short getId() {
        return Composers.GetFloorPlanUsedCoordsMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(tiles.size());

        for (Position position : tiles) {
            msg.writeInt(position.getX());
            msg.writeInt(position.getY());
        }
    }

    @Override
    public void dispose() {
        this.tiles.clear();
    }
}
