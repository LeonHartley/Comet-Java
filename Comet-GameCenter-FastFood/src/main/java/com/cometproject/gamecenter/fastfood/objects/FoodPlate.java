package com.cometproject.gamecenter.fastfood.objects;

import com.cometproject.gamecenter.fastfood.FastFoodGame;
import com.cometproject.gamecenter.fastfood.net.FastFoodGameSession;
import com.cometproject.gamecenter.fastfood.net.composers.DropFoodMessageComposer;

public class FoodPlate {
    private final int objectId;
    private final int playerId;
    private float location;
    private float speed;
    private int state;
    private long timeDropped = System.currentTimeMillis();

    public FoodPlate(int objectId, int playerId) {
        this.objectId = objectId;
        this.playerId = playerId;
        this.location = 1.0f;
        this.speed = 0.0f;
        this.state = 1;
    }

    public void tick(FastFoodGameSession gameSession, FastFoodGame game) {
        if((System.currentTimeMillis() - this.timeDropped) >= 1500) {
//            this.speed += 0.1;
//            this.location -= 0.1;

//            game.broadcast(new DropFoodMessageComposer(this));
            // we hit the table
            
        }
    }

    public int getObjectId() {
        return objectId;
    }

    public float getLocation() {
        return location;
    }

    public void setLocation(float location) {
        this.location = location;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPlayerId() {
        return playerId;
    }
}
