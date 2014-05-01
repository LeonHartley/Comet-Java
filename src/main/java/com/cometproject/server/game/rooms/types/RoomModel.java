package com.cometproject.server.game.rooms.types;

import com.cometproject.server.game.rooms.types.tiles.RoomTileState;
import com.cometproject.server.network.messages.outgoing.room.engine.HeightmapMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RelativeHeightmapMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomModel {
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

    public RoomModel(ResultSet data) throws SQLException {
        this.name = data.getString("id");
        this.heightmap = data.getString("heightmap");
        this.doorX = data.getInt("door_x");
        this.doorY = data.getInt("door_y");
        this.doorZ = data.getInt("door_z");
        this.doorRotation = data.getInt("door_dir");

        String[] temp = heightmap.split("\r");

        this.mapSizeX = temp[0].length();
        this.mapSizeY = temp.length;
        this.squares = new int[mapSizeX][mapSizeY];
        this.squareHeight = new double[mapSizeX][mapSizeY];
        this.squareState = new RoomTileState[mapSizeX][mapSizeY];

        // TODO: Add 'door' to room tile state

        try {
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
                        relativeMap += (int) doorZ + "";
                    } else {
                        if (Square.isEmpty() || Square == null) {
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

            this.loadMessages();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).error("Error while parsing room heightmap (Model ID: " + this.name + ")", e);
        }
    }

    private void loadMessages() {
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
            Integer.parseInt(Input);
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
