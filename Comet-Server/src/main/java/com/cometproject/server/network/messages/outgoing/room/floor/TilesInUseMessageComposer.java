package com.cometproject.server.network.messages.outgoing.room.floor;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;


public class TilesInUseMessageComposer extends MessageComposer {
    private final List<Position> tiles;

    public TilesInUseMessageComposer(final List<Position> tiles) {
        this.tiles = tiles;
    }

    @Override
    public short getId() {
        return Composers.FloorPlanFloorMapMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
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
