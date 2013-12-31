package com.cometsrv.game.wired.data.effects;

import com.cometsrv.game.wired.data.WiredDataInstance;
import javolution.util.FastList;

import java.util.List;

public class TeleportToItemData extends WiredDataInstance {
    private List<Integer> items;

    public TeleportToItemData(int id, int itemId, String data) {
        super("wf_act_moveuser", id, itemId);

        items = new FastList<>();

        for(String s : data.split(",")) {
            items.add(Integer.parseInt(s));
        }
    }

    public void addItem(int id) {
        this.items.add(id);
    }

    public boolean isMember(int id) {
        return items.contains(id);
    }
}
