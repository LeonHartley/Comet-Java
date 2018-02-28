package com.cometproject.game.rooms.models;

import com.cometproject.api.game.rooms.models.IRoomModel;
import com.cometproject.api.game.rooms.models.RoomModelData;
import com.cometproject.api.game.rooms.models.RoomTileState;

public class RoomModel implements IRoomModel {
    private static final char[] characters = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

    private final RoomModelData roomModelData;
    private final RoomTileState[][] squareStates;
    private final int[][] squareHeights;
    private final String roomMap;
    private final int doorZ;

    private final int sizeX;
    private final int sizeY;

    private String relativeHeightmap;

    public RoomModel(RoomModelData roomModelData, RoomTileState[][] squareStates, String map, int[][] squareHeights, int doorZ) {
        this.roomModelData = roomModelData;
        this.squareStates = squareStates;
        this.squareHeights = squareHeights;
        this.roomMap = map;
        this.doorZ = doorZ;

        this.sizeY = this.squareStates[0].length;
        this.sizeX = this.squareStates.length;
    }

    @Override
    public String getRelativeHeightmap() {
        if (this.relativeHeightmap != null) {
            return this.relativeHeightmap;
        }

        StringBuilder builder = new StringBuilder();

        for (int y = 0; y < this.sizeY; y++) {
            for (int x = 0; x < this.sizeX; x++) {
                if (this.getSquareState()[x][y] == RoomTileState.INVALID) {
                    builder.append("x");
                } else {
                    builder.append(characters[(int) Math.floor(this.getSquareHeight()[x][y] + 0.5d)]);
                }
            }

            builder.append((char) 13);
        }

        this.relativeHeightmap = builder.toString();
        return this.relativeHeightmap;
    }

    @Override
    public String getId() {
        return this.getRoomModelData().getName();
    }

    public RoomModelData getRoomModelData() {
        return roomModelData;
    }

    @Override
    public RoomTileState[][] getSquareState() {
        return squareStates;
    }

    @Override
    public int[][] getSquareHeight() {
        return squareHeights;
    }

    @Override
    public String getMap() {
        return this.roomMap;
    }

    @Override
    public int getDoorX() {
        return this.getRoomModelData().getDoorX();
    }

    @Override
    public int getDoorY() {
        return this.getRoomModelData().getDoorY();
    }

    @Override
    public int getDoorZ() {
        return this.doorZ;
    }

    @Override
    public int getSizeX() {
        return this.sizeX;
    }

    @Override
    public int getSizeY() {
        return this.sizeY;
    }

    @Override
    public int getDoorRotation() {
        return this.getRoomModelData().getDoorRotation();
    }
}
