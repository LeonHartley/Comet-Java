package com.cometproject.server.game.wired.data.effects;

import com.cometproject.server.game.wired.data.WiredDataInstance;
import javolution.util.FastList;

import java.util.List;

public class TeleportToItemData extends WiredDataInstance {
    private int delay;
    private List<Integer> items;

    public TeleportToItemData(int id, int itemId, String data) {
        super("wf_act_moveuser", id, itemId);

        this.items = new FastList<>();

        if(!data.isEmpty()) {
            String[] parse = data.split(":");
            this.delay = Integer.parseInt(parse[0]);

            for(String s : data.replace(this.delay + ":", "").split(",")) {
                this.items.add(Integer.parseInt(s));
            }
        } else {
            this.delay = 1; // 0.5s
        }
    }

    public List<Integer> getItems() {
        return this.items;
    }

    public int getCount() {
        return this.items.size();
    }

    public void addItem(int id) {
        this.items.add(id);
    }

    public boolean isMember(int id) {
        return this.items.contains(id);
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
