package com.cometproject.api.game.rooms.models;

public interface IRoomModel {

    String getRelativeHeightmap();

    String getId();

    RoomModelData getRoomModelData();

    RoomTileState[][] getSquareState();

    int[][] getSquareHeight();

    String getMap();

    int getDoorX();

    int getDoorY();

    int getDoorZ();

    int getSizeX();

    int getSizeY();

    int getDoorRotation();
}
