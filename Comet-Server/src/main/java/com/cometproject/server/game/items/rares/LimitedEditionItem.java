package com.cometproject.server.game.items.rares;

public class LimitedEditionItem {
    public static final LimitedEditionItem NONE = new LimitedEditionItem(0, 0, 0);

    private long itemId;
    private int limitedRare;
    private int limitedRareTotal;

    public LimitedEditionItem(long itemId, int limitedRare, int limitedRareTotal) {
        this.itemId = itemId;
        this.limitedRare = limitedRare;
        this.limitedRareTotal = limitedRareTotal;
    }

    public long getItemId() {
        return itemId;
    }

    public int getLimitedRare() {
        return limitedRare;
    }

    public int getLimitedRareTotal() {
        return limitedRareTotal;
    }
}
