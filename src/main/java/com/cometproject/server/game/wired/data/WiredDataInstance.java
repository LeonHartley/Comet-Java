package com.cometproject.server.game.wired.data;

import javolution.util.FastList;

import java.util.List;

public class WiredDataInstance {
    private int id;
    private int itemId;

    private int delay;
    private int movement; // wf_act_moverotate
    private int rotation; // wf_act_moverotate
    private List<Integer> items;
    public int cycles; // incremented ever tick

    public WiredDataInstance(int id, int itemId, String data) {
        this.id = id;
        this.itemId = itemId;

        this.items = new FastList<>();

        if(!data.isEmpty()) {
            String[] parse = data.split(":");
            this.delay = Integer.parseInt(parse[0]);
            this.movement = Integer.parseInt(parse[1]);
            this.rotation = Integer.parseInt(parse[2]);

            String[] items = data.replace(getDelay() + ":" + getMovement() + ":" + getRotation() + ":", "").split(",");

            if(!items[0].equals("")) {
                if(items.length > 0) {
                    for(String s : items) {
                        if(!s.isEmpty())
                            this.items.add(Integer.parseInt(s));
                    }
                }
            }
        } else {
            this.movement = 0;
            this.rotation = 0;
            this.delay = 1;
        }

        this.cycles = 0;
    }

    public void dispose() {
        this.items.clear();
        this.items = null;
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

    public int getRotation() {
        return this.rotation;
    }

    public int getMovement() {
        return this.movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
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
