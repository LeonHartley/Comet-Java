package com.cometproject.server.game.rooms.avatars.misc;

import com.cometproject.server.game.rooms.items.FloorItem;

public class Position3D {
    private int x;
    private int y;
    private double z;

    public Position3D(int x, int y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position3D(Position3D old) {
        this.x = old.getX();
        this.y = old.getY();
        this.z = old.getZ();
    }

    public Position3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0d;
    }

    public Position3D(int x, int y) {
        this.x = x;
        this.y = y;
        this.z = 0d;
    }

    public Position3D add(Position3D other) {
        return new Position3D(other.getX() + getX(), other.getY() + getY(), other.getZ() + getZ());
    }

    public Position3D subtract(Position3D other) {
        return new Position3D(other.getX() - getX(), other.getY() - getY(), other.getZ() - getZ());
    }

    public int getDistanceSquared(Position3D point) {
        int dx = this.getX() - point.getX();
        int dy = this.getY() - point.getY();

        return (dx * dx) + (dy * dy);
    }

    public static String validateWallPosition(String position) {
        try {
            String[] data = position.split(" ");
            if (data[2].equals("l") || data[2].equals("r")) {
                String[] width = data[0].substring(3).split(",");
                int widthX = Integer.parseInt(width[0]);
                int widthY = Integer.parseInt(width[1]);
                if (widthX < 0 || widthY < 0 || widthX > 200 || widthY > 200)
                    return null;

                String[] length = data[1].substring(2).split(",");
                int lengthX = Integer.parseInt(length[0]);
                int lengthY = Integer.parseInt(length[1]);
                if (lengthX < 0 || lengthY < 0 || lengthX > 200 || lengthY > 200)
                    return null;

                return ":w=" + widthX + "," + widthY + " " + "l=" + lengthX + "," + lengthY + " " + data[2];
            }
        } catch (Exception ignored) {

        }

        return null;
    }

    public static double calculateHeight(FloorItem item) {
        if(item.getDefinition().getInteraction().equals("gate")) {
            return 0;
        } else if(item.getDefinition().canSit) {
            return 0;
        }

        return item.getDefinition().getHeight();
    }

    public static int calculateRotation(int x, int y, int newX, int newY, boolean moonwalk) {
        int rotation = 0;

        if (x > newX && y > newY)
            rotation = 7;
        else if (x < newX && y < newY)
            rotation = 3;
        else if (x > newX && y < newY)
            rotation = 5;
        else if (x < newX && y > newY)
            rotation = 1;
        else if (x > newX)
            rotation = 6;
        else if (x < newX)
            rotation = 2;
        else if (y < newY)
            rotation = 4;
        else if (y > newY)
            rotation = 0;

        if(moonwalk) {
            if(rotation > 3) {
                rotation = rotation - 4;
            } else {
                rotation = rotation + 4;
            }
        }

        return rotation;
    }

    public Position3D squareInFront(int angle) {
        Position3D pos = new Position3D(x, y, 0);

        int posX = pos.getX();
        int posY = pos.getY();

        if(angle == 0) {
            posY--;
        } else if(angle == 2) {
            posX++;
        } else if(angle == 4) {
            posY++;
        } else if(angle == 6) {
            posX--;
        }

        pos.setX(posX);
        pos.setY(posY);

        return pos;
    }

    public Position3D squareBehind(int angle) {
        Position3D pos = new Position3D(x, y, 0);

        int posX = pos.getX();
        int posY = pos.getY();

        if(angle == 0) {
            posY++;
        } else if(angle == 2) {
            posX--;
        } else if(angle == 4) {
            posY--;
        } else if(angle == 6) {
            posX++;
        }

        pos.setX(posX);
        pos.setY(posY);

        return pos;
    }

    public static double distanceBetween(Position3D pos1, Position3D pos2) {
        int xDistance = pos2.getX() - pos1.getX();
        int yDistance = pos2.getY() - pos1.getY();

        return Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }

    @Override
    public String toString() {
        return "(" + this.getX() + ", " + this.getY() + ", " + this.getZ() + ")";
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
