package com.cometproject.gamecenter.fastfood.objects;

import com.cometproject.gamecenter.fastfood.FastFoodGame;
import com.cometproject.gamecenter.fastfood.net.FastFoodGameSession;
import com.cometproject.gamecenter.fastfood.net.composers.DropFoodMessageComposer;
import com.cometproject.gamecenter.fastfood.net.composers.FoodUpdateMessageComposer;
import com.cometproject.gamecenter.fastfood.net.composers.UseBigParachuteMessageComposer;

public class FoodPlate {
    private final int objectId;
    private final int playerId;
    private float location;
    private float speed;
    private int state;
    private long timeDropped = System.currentTimeMillis();

    private boolean finalized = false;

    public FoodPlate(int objectId, int playerId) {
        this.objectId = objectId;
        this.playerId = playerId;
        this.location = 1.0f;
        this.speed = 442.0f;
        this.state = 1;
    }

    public void tick(FastFoodGameSession gameSession, FastFoodGame game) {
        if((System.currentTimeMillis() - this.timeDropped) >= (this.state == 2 ? 4500 : 2500) && !finalized) {
            if(this.state == 2) { // we have a parachute
                this.state = 6;
            } else {
                this.state = 4;
            }

            game.broadcast(new FoodUpdateMessageComposer(this.playerId, game.getCounter().incrementAndGet(), this.state, 0, 0));

            finalized = true;
        }
    }

    public void openParachute(FastFoodGame game) {
        this.state = 2;

        long difference = System.currentTimeMillis() - this.timeDropped;

        this.location = this.location - ((difference / 1000) * (this.speed)); // calculate location
        this.speed = 0.1f;

        game.broadcast(new DropFoodMessageComposer(game.getCounter().getAndIncrement(), this));
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

    public boolean isFinalized() {
        return finalized;
    }
}
