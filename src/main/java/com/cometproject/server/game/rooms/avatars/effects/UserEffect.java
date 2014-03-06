package com.cometproject.server.game.rooms.avatars.effects;

public class UserEffect {
    private int effectId;
    private int duration;
    private boolean expires;
    private boolean isItemEffect;

    public UserEffect(int id, int duration) {
        this.effectId = id;
        this.duration = duration;
        this.expires = duration != 0;
        this.isItemEffect = false;
    }

    public UserEffect(int id, boolean isItemEffect) {
        this.effectId = id;
        this.isItemEffect = isItemEffect;
        this.duration = 0;
        this.expires = false;
    }

    public int getEffectId() {
        return this.effectId;
    }

    public int getDuration() {
        return this.duration;
    }

    public void decrementDuration() {
        if(this.duration > 0)
            this.duration--;
    }

    public boolean expires() {
        return this.expires;
    }

    public boolean isItemEffect() {
        return isItemEffect;
    }
}
