package com.cometsrv.game.wired.data.effects;

import com.cometsrv.game.wired.data.WiredDataInstance;

import java.util.List;

public class TeleportToItemData extends WiredDataInstance {
    private List<Integer> items;

    public TeleportToItemData(int id, int itemId, List<Integer> items) {
        super(id, itemId);

        this.items = items;
    }
}
