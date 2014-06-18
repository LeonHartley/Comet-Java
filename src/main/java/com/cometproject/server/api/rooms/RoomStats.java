package com.cometproject.server.api.rooms;

import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.RoomData;

public class RoomStats {
    private RoomData data;

    private int players;
    private int bots;
    private int pets;

    private long loadTime;

    public RoomStats(Room room) {
        this.data = room.getData();
        this.players = room.getEntities().playerCount();
        this.bots = room.getEntities().getBotEntities().size();
        this.pets = room.getEntities().getPetEntities().size();

        this.loadTime = (long) room.getAttribute("loadTime");
    }

    public RoomData getData() {
        return data;
    }

    public void setData(RoomData data) {
        this.data = data;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getBots() {
        return bots;
    }

    public void setBots(int bots) {
        this.bots = bots;
    }

    public int getPets() {
        return pets;
    }

    public void setPets(int pets) {
        this.pets = pets;
    }

    public long getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(long loadTime) {
        this.loadTime = loadTime;
    }
}
