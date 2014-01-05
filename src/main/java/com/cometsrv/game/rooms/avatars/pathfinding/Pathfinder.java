package com.cometsrv.game.rooms.avatars.pathfinding;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.tiles.RoomTileState;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;

public class Pathfinder
{
    /*
        @author Unknown
     */

    private Point[] points;

    public int goalX;
    public int goalY;
    private int mapX = 0;
    private int mapY = 0;

    private Avatar avatar;

    public Pathfinder(Avatar avatar)
    {
        points = new Point[] {
                new Point(0, -1),
                new Point(0, 1),
                new Point(1, 0),
                new Point(-1, 0),
                new Point(1, -1),
                new Point(-1, 1),
                new Point(1, 1),
                new Point(-1, -1)
        };

        mapX = avatar.getRoom().getModel().getSizeX();
        mapY = avatar.getRoom().getModel().getSizeY();

        this.avatar = avatar;
    }

    public LinkedList<Square> makePath()
    {
        LinkedList<Square> CoordSquares = new LinkedList<>();

        int UserX = avatar.getPosition().getX();
        int UserY = avatar.getPosition().getY();

        goalX = avatar.getGoalX();
        goalY = avatar.getGoalY();

        Square LastCoord = new Square(-200, -200);
        int Trys = 0;

        if(this.avatar.getIsTeleporting()) {
            CoordSquares.add(new Square(goalX, goalY));
            return CoordSquares;
        }

        while(true)
        {
            Trys++;
            int StepsToWalk = 10000;

            for (Point MovePoint : points)
            {
                int newX = MovePoint.x + UserX;
                int newY = MovePoint.y + UserY;

                if (newX >= 0 && newY >= 0 && mapX > newX && mapY > newY &&  avatar.getRoom().getModel().getSquareState()[newX][newY] == RoomTileState.VALID && avatar.getRoom().getAvatars().isSquareAvailable(newX, newY) && checkSquare(newX, newY))
                {
                    Square pCoord = new Square(newX, newY);
                    pCoord.positionDistance = DistanceBetween(newX, newY, goalX, goalY);
                    pCoord.reversedPositionDistance = DistanceBetween(goalX, goalY, newX, newY);

                    if (StepsToWalk > pCoord.positionDistance)
                    {
                        StepsToWalk = pCoord.positionDistance;
                        LastCoord = pCoord;
                    }
                }
            }
            if (Trys >= 200)
                return null;

            else if (LastCoord.x == -200 && LastCoord.y == -200)
                return null;

            UserX = LastCoord.x;
            UserY = LastCoord.y;
            CoordSquares.add(LastCoord);

            if(UserX == goalX && UserY == goalY)
                break;
        }
        return CoordSquares;
    }

    public void dispose() {
        Arrays.fill(points, null);
        avatar = null;
    }

    public boolean canWalk(int newX, int newY) {
        // Author: LH
        return newX >= 0 && newY >= 0 && mapX > newX && mapY > newY && avatar.getRoom().getModel().getSquareState()[newX][newY] == RoomTileState.VALID && avatar.getRoom().getAvatars().isSquareAvailable(newX, newY) && checkSquare(newX, newY);
    }

    public boolean checkSquare(int x, int y) {
        // Author: LH
        boolean isItem = false;
        boolean isAvailable = false;

        synchronized (avatar.getRoom().getItems().getItemsOnSquare(x, y)) {
            for(FloorItem item : avatar.getRoom().getItems().getItemsOnSquare(x, y)) {
                isItem = true;

                if(item.getDefinition().getInteraction().equals("gate") && item.getExtraData().equals("1")) {
                    isAvailable = true;
                } else if(item.getDefinition().getInteraction().equals("bed")) {
                    isAvailable = true;
                } else if(item.getDefinition().canSit) {
                    isAvailable = true;
                } else if(item.getDefinition().canWalk) {
                    isAvailable = true;
                }
            }
        }

        if(isItem && !isAvailable) {
            return false;
        }
        return true;
    }

    private int DistanceBetween(int currentX, int currentY, int goX, int goY)
    {
        return Math.abs(currentX - goX) + Math.abs(currentY - goY);
    }
}