package com.cometproject.api.game.furniture.types;

public class CrackableReward {
    private final int hitRequirement;
    private final CrackableRewardType rewardType;
    private final CrackableType crackableType;

    private final String rewardData;
    private final int rewardDataInt;

    public CrackableReward(int hitRequirement, CrackableRewardType rewardType, CrackableType crackableType, String rewardData, int rewardDataInt) {
        this.hitRequirement = hitRequirement;
        this.rewardType = rewardType;
        this.crackableType = crackableType;
        this.rewardData = rewardData;
        this.rewardDataInt = rewardDataInt;
    }

    public int getHitRequirement() {
        return hitRequirement;
    }

    public CrackableRewardType getRewardType() {
        return rewardType;
    }

    public CrackableType getCrackableType() {
        return crackableType;
    }

    public String getRewardData() {
        return rewardData;
    }

    public int getRewardDataInt() {
        return this.rewardDataInt;
    }
}
