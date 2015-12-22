package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;

public class UpdateStackMapMessageComposer extends MessageComposer {
    private final List<RoomTile> tilesToUpdate;
    private final RoomTile singleTile;

    public UpdateStackMapMessageComposer(final List<RoomTile> tilesToUpdate) {
        this.tilesToUpdate = tilesToUpdate;
        this.singleTile = null;
    }

    public UpdateStackMapMessageComposer(RoomTile tile) {
        this.tilesToUpdate = null;
        this.singleTile = tile;
    }

    @Override
    public short getId() {
        return Composers.UpdateStackMapMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeByte(singleTile != null ? 1 : tilesToUpdate.size());

        if (singleTile != null) {
            this.composeUpdate(this.singleTile, msg);
            return;
        }

        for (RoomTile tile : tilesToUpdate) {
            this.composeUpdate(tile, msg);
        }
    }

    private void composeUpdate(RoomTile tile, IComposer msg) {
        msg.writeByte(tile.getPosition().getX());
        msg.writeByte(tile.getPosition().getY());

        msg.writeShort((int) ((tile.getStackHeight()) * 256));
    }
}
