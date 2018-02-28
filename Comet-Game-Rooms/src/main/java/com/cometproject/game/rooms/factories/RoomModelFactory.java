package com.cometproject.game.rooms.factories;

import com.cometproject.api.game.rooms.models.*;
import com.cometproject.api.utilities.ModelUtils;
import com.cometproject.game.rooms.models.RoomModel;
import org.apache.log4j.Logger;

public class RoomModelFactory implements IRoomModelFactory {
    @Override
    public IRoomModel createModel(RoomModelData roomModelData) throws InvalidModelException {
        String[] axes = roomModelData.getHeightmap().split("\r");

        if (axes.length == 0) throw new InvalidModelException();

        final int mapSizeX = axes[0].length();
        final int mapSizeY = axes.length;
        final int[][] tileHeights = new int[mapSizeX][mapSizeY];
        final RoomTileState[][] tileStates = new RoomTileState[mapSizeX][mapSizeY];
        String map = "";

        int maxTileHeight = 0;
        int doorZ = 0;

        try {
            for (int y = 0; y < mapSizeY; y++) {
                char[] line = axes[y].replace("\r", "").replace("\n", "").toCharArray();

                int x = 0;
                for (char tile : line) {
                    if (x >= mapSizeX) {
                        throw new InvalidModelException();
                    }

                    String tileVal = String.valueOf(tile);

                    if (tileVal.equals("x")) {
                        tileStates[x][y] = (x == roomModelData.getDoorX() && y == roomModelData.getDoorY()) ? RoomTileState.VALID : RoomTileState.INVALID;
                    } else {
                        tileStates[x][y] = RoomTileState.VALID;
                        tileHeights[x][y] = ModelUtils.getHeight(tile);

                        if (tileHeights[x][y] > maxTileHeight) {
                            maxTileHeight = (int) Math.ceil(tileHeights[x][y]);
                        }
                    }

                    x++;
                }
            }

            doorZ = tileHeights[roomModelData.getDoorX()][roomModelData.getDoorY()];

            for (String mapLine : roomModelData.getHeightmap().split("\r\n")) {
                if (mapLine.isEmpty()) {
                    continue;
                }
                map += mapLine + (char) 13;
            }
        } catch (Exception e) {
            if (e instanceof InvalidModelException) {
                throw e;
            }

            Logger.getLogger(RoomModelFactory.class.getName()).error("Failed to parse heightmap for model: " + roomModelData.getName(), e);
        }

        if (maxTileHeight >= 29) {
            roomModelData.setWallHeight(15);
        }


        return new RoomModel(roomModelData, tileStates, map, tileHeights, doorZ);
    }
}
