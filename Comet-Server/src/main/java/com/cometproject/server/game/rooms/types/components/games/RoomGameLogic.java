package com.cometproject.server.game.rooms.types.components.games;

public abstract class RoomGameLogic {
    public abstract void tick(RoomGame game);

    public abstract void onGameStarts(RoomGame game);

    public abstract void onGameEnds(RoomGame game);
}
