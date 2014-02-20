package com.cometsrv.game.rooms.avatars.effects;

public class UserEffect {
    private int effectId;
    private int duration;
    private boolean expires;

    public UserEffect(int id, int duration) {
        this.effectId = id;
        this.duration = duration;
        this.expires = duration != 0;
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
}
