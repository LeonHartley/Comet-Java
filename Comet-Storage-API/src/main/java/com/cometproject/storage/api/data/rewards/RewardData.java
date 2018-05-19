package com.cometproject.storage.api.data.rewards;

public class RewardData {
    private String code;
    private String badge;
    private int diamonds;

    public RewardData(String code, String badge, int diamonds) {
        this.code = code;
        this.badge = badge;
        this.diamonds = diamonds;
    }

    public String getCode() {
        return code;
    }

    public String getBadge() {
        return badge;
    }

    public int getDiamonds() {
        return diamonds;
    }
}
