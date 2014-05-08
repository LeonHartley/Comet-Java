package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.game.rooms.models.RoomModel;
import com.cometproject.server.game.rooms.types.tiles.RoomTileState;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RelativeHeightmapMessageComposer {
    private static char[] characters;

    private static void init() {
        characters = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
    }

    public static Composer compose(RoomModel model) {
        if (characters == null) {
            init();
        }

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
                    builder.append(characters[(int) Math.floor(model.getSquareHeight()[x][y] + 0.5d)]);
                }
            }

            builder.append((char)13);
        }

        msg.writeString(builder.toString());
        return msg;
    }
}
