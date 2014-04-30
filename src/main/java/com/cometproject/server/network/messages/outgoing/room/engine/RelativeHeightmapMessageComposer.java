package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.game.rooms.types.RoomModel;
import com.cometproject.server.game.rooms.types.tiles.RoomTileState;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RelativeHeightmapMessageComposer {
    public static Composer compose(RoomModel model) {
        Composer msg = new Composer(Composers.RelativeHeightmapMessageComposer);

        msg.writeBoolean(true); // ??

        StringBuilder builder = new StringBuilder();

        for (int y = 0; y < model.getSizeY(); y++) {
            for (int x = 0; x < model.getSizeX(); x++) {
                if(x == model.getDoorX() && y == model.getDoorY()) {
                    builder.append(model.getDoorZ());
                } else if(model.getSquareState()[x][y] == RoomTileState.INVALID) {
                    builder.append("x");
                } else {
                    builder.append(Integer.toString((int) Math.floor(model.getSquareHeight()[x][y] + 0.5d), 36));
                }
            }

            builder.append((char)13);
        }

        msg.writeString(builder.toString());

        return msg;
    }
}
