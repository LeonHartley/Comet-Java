package com.cometsrv.game.rooms.avatars.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class AffectedTile
{
    public int x;
    public int y;
    public int I;

    public AffectedTile()
    {
        x = 0;
        y = 0;
        I = 0;
    }

    public AffectedTile(int x, int y, int i)
    {
        this.x = x;
        this.y = y;
        I = i;
    }

    public static List<AffectedTile> getAffectedTilesAt(int Length, int Width, int PosX, int PosY, int Rotation)
    {
        List<AffectedTile> pointList = new ArrayList<>();

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
        return pointList;
    }
}
