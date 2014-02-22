package com.cometproject.server.game.rooms.avatars.pathfinding;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.AvatarEntity;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;

public class Pathfinder {
    private Point[] points;

    public int goalX;
    public int goalY;

    protected AvatarEntity avatar;

    public Pathfinder(AvatarEntity avatar) {
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

        this.avatar = avatar;
    }

    public LinkedList<Square> makePath() {
        LinkedList<Square> coordSquares = new LinkedList<>();

        int userX = avatar.getPosition().getX();
        int userY = avatar.getPosition().getY();

        goalX = avatar.getWalkingGoal().getX();
        goalY = avatar.getWalkingGoal().getY();

        Square lastCoord = new Square(-200, -200);
        int trys = 0;

        while(true) {
            trys++;
            int stepsToWalk = 10000;

            for (Point movePoint : points) {
                int newX = movePoint.x + userX;
                int newY = movePoint.y + userY;

                Position3D currentPos = new Position3D(userX, userY, 0);
                Position3D newPos = new Position3D(newX, newY, 0);

                boolean lastStep = false;

                if(newY == goalY && newX == goalX) {
                    lastStep = true;
                }

                if (avatar.getRoom().getMapping().isValidStep(currentPos, newPos, lastStep)) {
                    Square square = new Square(newX, newY);
                    square.positionDistance = distanceBetween(newX, newY, goalX, goalY);
                    square.reversedPositionDistance = distanceBetween(goalX, goalY, newX, newY);

                    if (stepsToWalk > square.positionDistance)
                    {
                        stepsToWalk = square.positionDistance;
                        lastCoord = square;
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

    private int distanceBetween(int currentX, int currentY, int goX, int goY) {
        return Math.abs(currentX - goX) + Math.abs(currentY - goY);
    }
}