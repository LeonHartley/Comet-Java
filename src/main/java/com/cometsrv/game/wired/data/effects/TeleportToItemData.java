package com.cometsrv.game.wired.data.effects;

import com.cometsrv.game.wired.data.WiredDataInstance;

import java.util.List;

public class TeleportToItemData extends WiredDataInstance {
    private List<Integer> items;

    public TeleportToItemData(int id, int itemId, List<Integer> items) {
        super("wf_act_moveuser", id, itemId);

        this.items = items;
    }

    public void addItem(int id) {
        this.items.add(id);
    }

    public boolean isMember(int id) {
        return items.contains(id);
    }
}
