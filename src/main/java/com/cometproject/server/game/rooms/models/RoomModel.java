package com.cometproject.server.game.rooms.models;

import com.cometproject.server.game.rooms.types.tiles.RoomTileState;
import com.cometproject.server.game.utilities.ModelUtils;
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

        //String[] axes = heightmap.split(String.valueOf((char) 13));
        String[] axes = heightmap.split("\r");

        this.mapSizeX = axes[0].length();
        this.mapSizeY = axes.length;
        this.squares = new int[mapSizeX][mapSizeY];
        this.squareHeight = new double[mapSizeX][mapSizeY];
        this.squareState = new RoomTileState[mapSizeX][mapSizeY];

        System.out.println("[" + this.name + "] Size X " + mapSizeX + ", Size Y: " + mapSizeY);

        try {
            for (int y = 0; y < mapSizeY; y++) {
                char[] line = axes[y].replace("\r", "").replace("\n", "").toCharArray();

                int x = 0;
                for(char tile : line) {
                    String tileVal = String.valueOf(tile);

                    if(tileVal.equals("x")) {

                        squareState[x][y] = RoomTileState.INVALID;
                        squares[x][y] = closed;
                    } else {
                        squareState[x][y] = RoomTileState.VALID;
                        squareHeight[x][y] = (double) ModelUtils.getHeight(tile);
                    }

                    x++;
                }

                relativeMap += (char) 13;
            }

            for (String mapLine : heightmap.split("\r\n")) {
                if (mapLine.isEmpty() || mapLine == null) {
                    continue;
                }
                map += mapLine + (char) 13;
            }

            this.heightmapMessage = HeightmapMessageComposer.compose(this);
            this.relativeHeightmapMessage = RelativeHeightmapMessageComposer.compose(this);
        } catch(Exception e) {
            System.out.println("Failed to load model: " + this.name);
        }
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

    private boolean isNumeric(String input) {
        try {
            int i = Integer.parseInt(input);
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
