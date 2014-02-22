package com.cometproject.server.game.rooms.avatars.pathfinding;

public class Square {
    public int x;
    public int y;

    public int positionDistance = 1000;
    public int reversedPositionDistance = 1000;

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
