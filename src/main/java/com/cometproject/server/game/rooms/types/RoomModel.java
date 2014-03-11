package com.cometproject.server.game.rooms.types;

import com.cometproject.server.game.rooms.types.tiles.RoomTileState;
import com.cometproject.server.network.messages.types.Composer;
import com.google.common.base.Charsets;

import java.nio.charset.Charset;
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
    private short[][] squareHeight;
    private RoomTileState[][] squareState;

    public RoomModel(ResultSet data) throws SQLException {
        this.name = data.getString("id");
        this.heightmap = data.getString("heightmap");
        this.doorX = data.getInt("door_x");
        this.doorY = data.getInt("door_y");
        this.doorZ = data.getInt("door_z");
        this.doorRotation = data.getInt("door_dir");

        String[] temp = heightmap.split(Character.toString((char)13));

        this.mapSizeX = temp[0].length();
        this.mapSizeY = temp.length;
        this.squares = new int[mapSizeX][mapSizeY];
        this.squareHeight = new short[mapSizeX][mapSizeY];
        this.squareState = new RoomTileState[mapSizeX][mapSizeY];

        // TODO: Add 'door' to room tile state

        for (int y = 0; y < mapSizeY; y++) {
            if (y > 0) {
                temp[y] = temp[y].substring(1);
            }

            for (int x = 0; x < mapSizeX; x++) {
                String Square = temp[y].substring(x,x + 1).trim().toLowerCase();

                if (Square.equals("x")) {
                    squareState[x][y] = RoomTileState.INVALID;
                    squares[x][y] = closed;
                }
                else if(isNumeric(Square))  {
                    squareState[x][y] = RoomTileState.VALID;
                    squares[x][y] = open;
                    squareHeight[x][y] = Short.parseShort(Square);
                    mapSize++;
                }

                if (doorX == x && doorY == y) {
                    squareState[x][y] = RoomTileState.VALID;
                    relativeMap += (int)doorZ + "";
                }
                else {
                    if(Square.isEmpty() || Square == null) {
                        continue;
                    }
                    relativeMap += Square;
                }
            }
            relativeMap += (char)13;
        }

        for(String MapLine: heightmap.split("\r\n"))
        {
            if(MapLine.isEmpty() || MapLine == null)
            {
                continue;
            }
            map += MapLine + (char)13;
        }
    }

    public void composeRelative(Composer msg) {
        int area = mapSizeX * mapSizeY;

        msg.writeInt(mapSizeX);
        msg.writeInt(area);

        for(int y = 0; y < mapSizeY; y++) {
            for(int x = 0; x < mapSizeX; x++) {
                if(squareState[x][y].equals(RoomTileState.INVALID))
                    msg.writeShort(-1);
                else
                    msg.writeShort(squareHeight[x][y] << 8);
            }
        }
    }

    public void compose(Composer msg) {
        StringBuilder builder = new StringBuilder();

        for(int y = 0; y < mapSizeY; y++) {
            for(int x = 0; x < mapSizeX; x++) {
                if(squareState[x][y] == RoomTileState.INVALID) {
                    builder.append("x");
                } else if(x == doorX && y == doorY) {
                    builder.append(doorZ);
                } else {
                    builder.append(squareHeight[x][y]);
                }
            }
        }

        String[] map = builder.toString().split("\r");

        msg.writeInt(map[0].length());
        msg.writeInt((map.length - 1) * map[0].length());

        for(int y = 0; y < map.length - 1; y++) {
            for(int x = 0; x < map[0].length(); x++) {
                String tile = map[y];
                System.out.println(tile);
            }
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

    public short[][] getSquareHeight() {
        return this.squareHeight;
    }

    private boolean isNumeric(String Input) {
        try {
            Integer.parseInt(Input);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
