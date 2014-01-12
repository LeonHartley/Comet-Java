package com.cometsrv.game.rooms.avatars.pathfinding;

import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.entities.AvatarEntity;

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

    protected AvatarEntity avatar;

    public Pathfinder(AvatarEntity avatar)
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
        LinkedList<Square> coordSquares = new LinkedList<>();

        int userX = avatar.getPosition().getX();
        int userY = avatar.getPosition().getY();

        goalX = avatar.getWalkingGoal().getX();
        goalY = avatar.getWalkingGoal().getY();

        Square lastCoord = new Square(-200, -200);
        int trys = 0;

        while(true)
        {
            trys++;
            int stepsToWalk = 10000;

            for (Point movePoint : points)
            {
                int newX = movePoint.x + userX;
                int newY = movePoint.y + userY;

                //if (newX >= 0 && newY >= 0 && mapX > newX && mapY > newY &&  avatar.getRoom().getModel().getSquareState()[newX][newY] == RoomTileState.VALID && avatar.getRoom().getEntities().isSquareAvailable(newX, newY) && checkSquare(newX, newY))
                if (avatar.getRoom().getMapping().isValidStep(new Position3D(userX, userY, 0), new Position3D(newX, newY, 0), false))
                {
                    Square pCoord = new Square(newX, newY);
                    pCoord.positionDistance = DistanceBetween(newX, newY, goalX, goalY);
                    pCoord.reversedPositionDistance = DistanceBetween(goalX, goalY, newX, newY);

                    if (stepsToWalk > pCoord.positionDistance)
                    {
                        stepsToWalk = pCoord.positionDistance;
                        lastCoord = pCoord;
                    }
                }
            }
            if (trys >= 200) {
                return null;
            } else if (lastCoord.x == -200 && lastCoord.y == -200) {
                return null;
            }

            userX = lastCoord.x;
            userY = lastCoord.y;
            coordSquares.add(lastCoord);

            if(userX == goalX && userY == goalY) {
                break;
            }
        }
        return coordSquares;
    }

    public void dispose() {
        Arrays.fill(points, null);
        avatar = null;
    }

    /*public boolean canWalk(int newX, int newY) {
        // Author: LH
        return newX >= 0 && newY >= 0 && mapX > newX && mapY > newY && avatar.getRoom().getModel().getSquareState()[newX][newY] == RoomTileState.VALID && avatar.getRoom().getEntities().isSquareAvailable(newX, newY) && checkSquare(newX, newY);
    }*/

    /*public boolean checkSquare(int x, int y) {
        // Author: LH
        boolean isItem = false;
        boolean isAvailable = false;

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

        if(isItem && !isAvailable) {
            return false;
        }
        return true;
    }*/

    private int DistanceBetween(int currentX, int currentY, int goX, int goY)
    {
        return Math.abs(currentX - goX) + Math.abs(currentY - goY);
    }
}