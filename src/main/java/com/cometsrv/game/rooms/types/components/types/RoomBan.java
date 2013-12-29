package com.cometsrv.game.rooms.types.components.types;

public class RoomBan {
    private int id, cycle;

    public RoomBan(int id) {
        this.id = id;
        this.cycle = 0; // gets increased on every cycle
    }

    public int getId() {
        return this.id;
    }

    public int getCycle() {
        return this.cycle;
    }

    public void increaseCycle() {
        this.cycle++;
    }
}
