package com.cometproject.server.game.rooms.items;

public interface GenericRoomItem {
    public int getId();
    public int getItemId();
    public int getOwner();

    public int getX();
    public int getY();

    public int getRotation();

    public boolean getState();
}