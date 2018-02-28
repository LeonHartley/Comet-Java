package com.cometproject.server.game.rooms.types.components.games.freeze.types;

import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.outgoing.room.freeze.UpdateFreezeLivesMessageComposer;

public class FreezePlayer {
    private final PlayerEntity entity;

    private FreezePowerUp powerUp = FreezePowerUp.None;
    private int lives = 3;
    private int balls = 1;

    private int shieldTimer = 0;
    private int freezeTimer = 0;

    public FreezePlayer(PlayerEntity playerEntity) {
        this.entity = playerEntity;
    }

    public void powerUp(FreezePowerUp powerUp) {
        switch (powerUp) {
            case Life: {
                this.lives++;

                this.entity.getRoom().getEntities().broadcastMessage(new UpdateFreezeLivesMessageComposer(this.entity.getId(), this.lives));
                return;
            }

            case ExtraBall: {
                this.balls++;
                return;
            }

            case Shield: {
                this.shieldTimer = 10;
                this.entity.applyEffect(new PlayerEffect(this.getEntity().getGameTeam().getTeamId() + 48, 20));
            }
        }

        this.powerUp = powerUp;
    }

    public PlayerEntity getEntity() {
        return entity;
    }

    public FreezePowerUp getPowerUp() {
        return powerUp;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void decrementLives() {
        this.lives--;
    }

    public int getBalls() {
        return balls;
    }

    public void setBalls(int balls) {
        this.balls = balls;
    }

    public void incrementBalls() {
        this.balls += 1;
    }

    public int getShieldTimer() {
        return shieldTimer;
    }

    public void decrementShieldTimer() {
        this.shieldTimer--;
    }

    public int getFreezeTimer() {
        return freezeTimer;
    }

    public void setFreezeTimer(int freezeTimer) {
        this.freezeTimer = freezeTimer;
    }

    public void decrementFreezeTimer() {
        this.freezeTimer--;
    }
}
