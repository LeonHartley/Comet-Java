package com.cometproject.server.game.wired.data;

import javolution.util.FastList;

import java.util.List;

public class WiredDataInstance {
    private int id;
    private int itemId;

    private int delay;
    private List<Integer> items;

    public WiredDataInstance(int id, int itemId, String data) {
        this.id = id;
        this.itemId = itemId;

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

    public int getId() {
        return this.id;
    }

    public int getItemId() {
        return this.itemId;
    }

    public List<Integer> getItems() {
        return this.items;
    }

    public int getDelay() {
        return this.delay;
    }

    public void addItem(int id) {
        this.items.add(id);
    }

    public boolean isMember(int id) {
        return this.items.contains(id);
    }

    public int getCount() {
        return this.items.size();
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
