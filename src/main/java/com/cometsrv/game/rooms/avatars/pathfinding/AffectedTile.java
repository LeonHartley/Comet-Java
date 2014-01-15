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

        /*List<AffectedTile> pointList = new ArrayList<>();

        if (Length > 1)
        {
            if (Rotation == 0 || Rotation == 4)
            {
                for (int i = 1; i < Length; i++)
                {
                    pointList.add(new AffectedTile(PosX, PosY + i, i));

                    for (int j = 1; j < Width; j++)
                    {
                        pointList.add(new AffectedTile(PosX + j, PosY + i, (i < j) ? j : i));
                    }
                }
            }
            else if (Rotation == 2 || Rotation == 6)
            {
                for (int i = 1; i < Length; i++)
                {
                    pointList.add(new AffectedTile(PosX + i, PosY, i));

                    for (int j = 1; j < Width; j++)
                    {
                        pointList.add(new AffectedTile(PosX + i, PosY + j, (i < j) ? j : i));
                    }
                }
            }
        }

        if (Width > 1)
        {
            if (Rotation == 0 || Rotation == 4)
            {
                for (int i = 1; i < Width; i++)
                {
                    pointList.add(new AffectedTile(PosX + i, PosY, i));

                    for (int j = 1; j < Length; j++)
                    {
                        pointList.add(new AffectedTile(PosX + i, PosY + j, (i < j) ? j : i));
                    }
                }
            }
            else if (Rotation == 2 || Rotation == 6)
            {
                for (int i = 1; i < Width; i++)
                {
                    pointList.add(new AffectedTile(PosX, PosY + i, i));

                    for (int j = 1; j < Length; j++)
                    {
                        pointList.add(new AffectedTile(PosX + j, PosY + i, (i < j) ? j : i));
                    }
                }
            }
        }

        return pointList;*/
    }
}
