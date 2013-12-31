package com.cometsrv.game.rooms.avatars.misc;

import com.cometsrv.game.rooms.items.FloorItem;

public class Position {
    private int x;
    private int y;
    private double z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
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

    public static Position getSquareInFront(FloorItem item) {
        Position pos = new Position(item.getX(), item.getY(), 0);

        int posX = pos.getX();
        int posY = pos.getY();

        if(item.getRotation() == 0) {
            posX--;
        } else if(item.getRotation() == 2) {
            posX++;
        } else if(item.getRotation() == 4) {
            posY++;
        } else if(item.getRotation() == 6) {
            posX--;
        }

        pos.setX(posX);
        pos.setY(posY);

        return pos;
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
