package com.cometproject.server.game.rooms.objects.entities.pathfinding;

import java.util.ArrayList;
import java.util.List;


public class AffectedTile {
    public int x;
    public int y;

    public AffectedTile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static List<AffectedTile> getAffectedBothTilesAt(int length, int width, int posX, int posY, int rotation) {
        List<AffectedTile> pointList = new ArrayList<>();

        pointList.add(new AffectedTile(posX, posY));

        if (length > 1) {
            if (rotation == 0 || rotation == 4) {
                for (int i = 1; i < length; i++) {
                    pointList.add(new AffectedTile(posX, posY + i));

                    for (int j = 1; j < width; j++) {
                        pointList.add(new AffectedTile(posX + j, posY + i));
                    }
                }
            } else if (rotation == 2 || rotation == 6) {
                for (int i = 1; i < length; i++) {
                    pointList.add(new AffectedTile(posX + i, posY));

                    for (int j = 1; j < width; j++) {
                        pointList.add(new AffectedTile(posX + i, posY + j));
                    }
                }
            }
        }

        if (width > 1) {
            if (rotation == 0 || rotation == 4) {
                for (int i = 1; i < width; i++) {
                    pointList.add(new AffectedTile(posX + i, posY));

                    for (int j = 1; j < length; j++) {
                        pointList.add(new AffectedTile(posX + i, posY + j));
                    }
                }
            } else if (rotation == 2 || rotation == 6) {
                for (int i = 1; i < width; i++) {
                    pointList.add(new AffectedTile(posX, posY + i));

                    for (int j = 1; j < length; j++) {
                        pointList.add(new AffectedTile(posX + j, posY + i));
                    }
                }
            }
        }

        return pointList;
    }

    public static List<AffectedTile> getAffectedTilesAt(int length, int width, int posX, int posY, int rotation) {
        List<AffectedTile> pointList = new ArrayList<>();

        if (length > 1) {
            if (rotation == 0 || rotation == 4) {
                for (int i = 1; i < length; i++) {
                    pointList.add(new AffectedTile(posX, posY + i));

                    for (int j = 1; j < width; j++) {
                        pointList.add(new AffectedTile(posX + j, posY + i));
                    }
                }
            } else if (rotation == 2 || rotation == 6) {
                for (int i = 1; i < length; i++) {
                    pointList.add(new AffectedTile(posX + i, posY));

                    for (int j = 1; j < width; j++) {
                        pointList.add(new AffectedTile(posX + i, posY + j));
                    }
                }
            }
        }

        if (width > 1) {
            if (rotation == 0 || rotation == 4) {
                for (int i = 1; i < width; i++) {
                    pointList.add(new AffectedTile(posX + i, posY));

                    for (int j = 1; j < length; j++) {
                        pointList.add(new AffectedTile(posX + i, posY + j));
                    }
                }
            } else if (rotation == 2 || rotation == 6) {
                for (int i = 1; i < width; i++) {
                    pointList.add(new AffectedTile(posX, posY + i));

                    for (int j = 1; j < length; j++) {
                        pointList.add(new AffectedTile(posX + j, posY + i));
                    }
                }
            }
        }

        return pointList;
    }
}