package com.cometsrv.game.rooms.entities;

import com.cometsrv.game.rooms.avatars.misc.Position;
import com.cometsrv.game.rooms.types.Room;

public abstract class GenericEntity {
    private int id;
    private Position position;
    private Room room;

    public GenericEntity(Position position, Room room) {
        this.id = room.getEntities().getFreeId();
        this.position = position;
    }

    public abstract void joinRoom();

    public abstract void leaveRoom();

    public abstract void process();

    public int getId() {
        return this.id;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Room getRoom() {
        return this.room;
    }
}
