package com.cometproject.server.game.items.rares;

public class LimitedEditionItem {
    private int itemId;
    private int limitedRare;
    private int limitedRareTotal;

    public LimitedEditionItem(int itemId, int limitedRare, int limitedRareTotal) {
        this.itemId = itemId;
        this.limitedRare = limitedRare;
        this.limitedRareTotal = limitedRareTotal;
    }

    public int getItemId() {
        return itemId;
    }

    public int getLimitedRare() {
        return limitedRare;
    }

    public int getLimitedRareTotal() {
        return limitedRareTotal;
    }
}
