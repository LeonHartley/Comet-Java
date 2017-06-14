package com.cometproject.server.game.rooms.types.components.games.freeze;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.games.GameType;
import com.cometproject.server.game.rooms.types.components.games.RoomGame;

public class FreezeGame extends RoomGame {
    public FreezeGame(Room room) {
        super(room, GameType.FREEZE);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onGameEnds() {

    }

    @Override
    public void onGameStarts() {

    }
}
