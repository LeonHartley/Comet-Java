package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.types.mapping.RoomTile;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.List;

public class UpdateStackMapMessageComposer extends MessageComposer {
    private final List<RoomTile> tilesToUpdate;
    private final RoomTile singleTile;
    private Double overrideHeight;

    public UpdateStackMapMessageComposer(final List<RoomTile> tilesToUpdate, double overrideHeight) {
        this.tilesToUpdate = tilesToUpdate;
        this.singleTile = null;
        this.overrideHeight = overrideHeight;
    }

    public UpdateStackMapMessageComposer(RoomTile tile, double overrideHeight) {
        this.singleTile = tile;
        this.overrideHeight = overrideHeight;
        this.tilesToUpdate = null;
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

        msg.writeShort((int) ((this.overrideHeight != null ? this.overrideHeight : tile.getStackHeight()) * 256));
    }
}
