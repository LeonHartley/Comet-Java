package com.cometsrv.game.rooms.avatars.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class AffectedTile
{
    public int x;
    public int y;

    public AffectedTile(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public static List<AffectedTile> getAffectedTilesAt(int length, int width, int posX, int posY, int rotation)
    {
    //old
       List<AffectedTile> points = new ArrayList<>();

        if (length > 0) {
            if (rotation == 0 || rotation == 4) {
                for (int pY = 1; pY < length; pY++) {
                    points.add(new AffectedTile(posX, posY));

                    for (int pX = 1; pX < width; pX++) {
                        points.add(new AffectedTile(posX + pX, posY + pY));
                    }
                }
            }
            else if (rotation == 2 || rotation == 6) {
                for (int pX = 1; pX < width; pX++) {
                    points.add(new AffectedTile(posX + pX, posY));

                    for (int pY = 1; pY < length; pY++) {
                        points.add(new AffectedTile(posX + pX, posY + pY));
                    }
                }

            }
        }

        if (width > 0) {
            if (rotation == 0 || rotation == 4) {
                for (int pX = 1; pX < width; pX++) {
                    AffectedTile aTile = new AffectedTile(posX + pX, posY);

                    if (!points.contains(aTile)) {
                        points.add(aTile);
                    }

                    for (int pY = 1; pY < length; pY++) {
                        AffectedTile aTile0 = new AffectedTile(posX + pX, posY + pY);

                        if (!points.contains(aTile0)) {
                            points.add(aTile0);
                        }
                    }
                }
            } else if (rotation == 2 || rotation == 6) {
                for (int pY = 1; pY < length; pY++) {
                    AffectedTile aTile = new AffectedTile(posX, posY + pY);

                    if (!points.contains(aTile)) {
                        points.add(aTile);
                    }

                    for (int pX = 1; pX < width; pX++) {
                        AffectedTile aTile0 = new AffectedTile(posX + pX, posY + pY);

                        if (!points.contains(aTile0)) {
                            points.add(aTile0);
                        }
                    }
                }
            }
        }

        return points;
        //endofold
/*
        List<AffectedTile> pointList = new ArrayList<>();

        if (length > 1)
        {
            if (rotation == 0 || rotation == 4)
            {
                for (int i = 1; i < length; i++)
                {
                    pointList.add(new AffectedTile(posX, posY + i));

                    for (int j = 1; j < width; j++)
                    {
                        pointList.add(new AffectedTile(posX + j, posY + i));
                    }
                }
            }
            else if (rotation == 2 || rotation == 6)
            {
                for (int i = 1; i < length; i++)
                {
                    pointList.add(new AffectedTile(posX + i, posY));

                    for (int j = 1; j < width; j++)
                    {
                        pointList.add(new AffectedTile(posX + i, posY + j));
                    }
                }
            }
        }

        if (width > 1)
        {
            if (rotation == 0 || rotation == 4)
            {
                for (int i = 1; i < width; i++)
                {
                    pointList.add(new AffectedTile(posX + i, posY));

                    for (int j = 1; j < length; j++)
                    {
                        pointList.add(new AffectedTile(posX + i, posY + j));
                    }
                }
            }
            else if (rotation == 2 || rotation == 6)
            {
                for (int i = 1; i < width; i++)
                {
                    pointList.add(new AffectedTile(posX, posY + i));

                    for (int j = 1; j < length; j++)
                    {
                        pointList.add(new AffectedTile(posX + j, posY + i));
                    }
                }
            }
        }

        return pointList;*/
    }
}