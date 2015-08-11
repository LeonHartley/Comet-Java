package com.cometproject.server.game.catalog.purchase;

public class PurchaseResult {
    private int amount;
    private String extraData;

    public PurchaseResult(int amount, String extraData) {
        this.amount = amount;
        this.extraData = extraData;
    }

    public int getAmount() {
        return amount;
    }

    public String getExtraData() {
        return this.extraData;
    }
}
