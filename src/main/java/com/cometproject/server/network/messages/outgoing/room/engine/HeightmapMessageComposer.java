package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.game.rooms.models.RoomModel;
import com.cometproject.server.game.rooms.types.tiles.RoomTileState;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class HeightmapMessageComposer extends MessageComposer {
    private final RoomModel roomModel;

    public HeightmapMessageComposer(final RoomModel roomModel) {
        this.roomModel = roomModel;
    }

    @Override
    public short getId() {
        return Composers.HeightMapMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(roomModel.getSizeX());
        msg.writeInt(roomModel.getSizeY() * roomModel.getSizeX());

        for (int y = 0; y < roomModel.getSizeY(); y++) {
            for (int x = 0; x < roomModel.getSizeX(); x++) {
                if (roomModel.getSquareState()[x][y] == RoomTileState.INVALID) {
                    msg.writeShort(16191);
                } else if (roomModel.getDoorY() == y && roomModel.getDoorX() == x) {
                    msg.writeShort(0);
                } else {
                    msg.writeShort((short) roomModel.getSquareHeight()[x][y]);
                }
            }
        }
    }
}
