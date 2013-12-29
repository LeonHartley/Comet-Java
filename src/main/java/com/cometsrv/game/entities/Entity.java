package com.cometsrv.game.entities;

public abstract class Entity {
    private int id, virtualId, x, y;
    private double z;
    private EntityType type;

    // TODO: implement new entity system (this code isn't in-use yet..)

    public Entity(int id, int virtualId, int x, int y, double z) {
        this.id = id;
        this.virtualId = virtualId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getId() {
        return this.id;
    }

    public int getVirtualId() {
        return this.virtualId;
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
