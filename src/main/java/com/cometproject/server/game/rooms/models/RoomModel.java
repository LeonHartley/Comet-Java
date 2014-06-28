package com.cometproject.server.game.rooms.models;

import com.cometproject.server.game.rooms.types.tiles.RoomTileState;
import com.cometproject.server.game.utilities.ModelUtils;
import com.cometproject.server.network.messages.outgoing.room.engine.HeightmapMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RelativeHeightmapMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import org.apache.log4j.Logger;

import java.util.Arrays;

public abstract class RoomModel {
    private String name;
    private String map = "";

    private int doorX;
    private int doorY;
    private int doorZ;
    private int doorRotation;

    private int mapSizeX;
    private int mapSizeY;

    private double[][] squareHeight;
    private RoomTileState[][] squareState;

    public RoomModel(String name, String heightmap, int doorX, int doorY, int doorZ, int doorRotation) {
        this.name = name;
        this.doorX = doorX;
        this.doorY = doorY;
        this.doorZ = doorZ;
        this.doorRotation = doorRotation;

        String[] axes = heightmap.split("\r");

        this.mapSizeX = axes[0].length();
        this.mapSizeY = axes.length;
        this.squareHeight = new double[mapSizeX][mapSizeY];
        this.squareState = new RoomTileState[mapSizeX][mapSizeY];

        try {
            for (int y = 0; y < mapSizeY; y++) {
                char[] line = axes[y].replace("\r", "").replace("\n", "").toCharArray();

                int x = 0;
                for (char tile : line) {
                    String tileVal = String.valueOf(tile);

                    if (tileVal.equals("x")) {
                        squareState[x][y] = (x == doorX && y == doorY) ? RoomTileState.VALID : RoomTileState.INVALID;
                    } else {
                        squareState[x][y] = RoomTileState.VALID;
                        squareHeight[x][y] = (double) ModelUtils.getHeight(tile);
                    }

                    x++;
                }
            }

            for (String mapLine : heightmap.split("\r\n")) {
                if (mapLine.isEmpty() || mapLine == null) {
                    continue;
                }
                map += mapLine + (char) 13;
            }
        } catch (Exception e) {
            Logger.getLogger(RoomModel.class.getName()).error("Failed to parse heightmap for model: " + this.name, e);
        }
    }

    public void dispose() {
        Arrays.fill(this.squareHeight, null);
        Arrays.fill(this.squareState, null);
    }

    public String getId() {
        return this.name;
    }

    public String getMap() {
        return this.map;
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

    public Composer getHeightmapMessage() {
        return HeightmapMessageComposer.compose(this);
    }

    public Composer getRelativeHeightmapMessage() {
        return RelativeHeightmapMessageComposer.compose(this);
    }
}
