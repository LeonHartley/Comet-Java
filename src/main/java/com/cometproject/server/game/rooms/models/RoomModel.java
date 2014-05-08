package com.cometproject.server.game.rooms.models;

import com.cometproject.server.game.rooms.types.tiles.RoomTileState;
import com.cometproject.server.network.messages.outgoing.room.engine.HeightmapMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RelativeHeightmapMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class RoomModel {
    private String name;
    private String heightmap;
    private String map = "";
    private String relativeMap = "";

    private int doorX;
    private int doorY;
    private int doorZ;
    private int doorRotation;

    private int mapSizeX;
    private int mapSizeY;
    private int mapSize;

    private int open = 0;
    private int closed = 1;

    private int[][] squares;
    private double[][] squareHeight;
    private RoomTileState[][] squareState;

    private Composer heightmapMessage;
    private Composer relativeHeightmapMessage;

    public RoomModel(String name, String heightmap, int doorX, int doorY, int doorZ, int doorRotation) {
        this.name = name;
        this.heightmap = heightmap;
        this.doorX = doorX;
        this.doorY = doorY;
        this.doorZ = doorZ;
        this.doorRotation = doorRotation;

        String[] temp = heightmap.split("\r");

        this.mapSizeX = temp[0].length();
        this.mapSizeY = temp.length;
        this.squares = new int[mapSizeX][mapSizeY];
        this.squareHeight = new double[mapSizeX][mapSizeY];
        this.squareState = new RoomTileState[mapSizeX][mapSizeY];

        for (int y = 0; y < mapSizeY; y++) {
            if (y > 0) {
                temp[y] = temp[y].substring(1);
            }

            for (int x = 0; x < mapSizeX; x++) {
                String Square = temp[y].substring(x, x + 1).trim().toLowerCase();

                if (Square.equals("x")) {
                    squareState[x][y] = RoomTileState.INVALID;
                    squares[x][y] = closed;
                } else if (isNumeric(Square)) {
                    squareState[x][y] = RoomTileState.VALID;
                    squares[x][y] = open;
                    squareHeight[x][y] = Double.parseDouble(Square);
                    mapSize++;
                }

                if (doorX == x && doorY == y) {
                    squareState[x][y] = RoomTileState.VALID;
                    relativeMap += doorZ;
                } else {
                    if (Square.isEmpty()) {
                        continue;
                    }
                    relativeMap += Square;
                }
            }
            relativeMap += (char) 13;
        }

        for (String MapLine : heightmap.split("\r\n")) {
            if (MapLine.isEmpty() || MapLine == null) {
                continue;
            }
            map += MapLine + (char) 13;
        }

        this.heightmapMessage = HeightmapMessageComposer.compose(this);
        this.relativeHeightmapMessage = RelativeHeightmapMessageComposer.compose(this);
    }

    public String getId() {
        return this.name;
    }

    public String getHeightmap() {
        return this.heightmap;
    }

    public String getMap() {
        return this.map;
    }

    public String getRelativeMap() {
        return this.relativeMap;
    }

    public int getDoorX() {
        return this.doorX;
    }

    public int getDoorY() {
        return this.doorY;
    }

    public int getDoorZ() {
        return this.doorZ;
    }

    public int getDoorRotation() {
        return this.doorRotation;
    }

    public int getSizeX() {
        return this.mapSizeX;
    }

    public int getSizeY() {
        return this.mapSizeY;
    }

    public RoomTileState[][] getSquareState() {
        return this.squareState;
    }

    public double[][] getSquareHeight() {
        return this.squareHeight;
    }

    private boolean isNumeric(String Input) {
        try {
            int i = Integer.parseInt(Input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Composer getHeightmapMessage() {
        return heightmapMessage;
    }

    public Composer getRelativeHeightmapMessage() {
        return relativeHeightmapMessage;
    }
}
