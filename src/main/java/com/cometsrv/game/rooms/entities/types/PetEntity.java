package com.cometsrv.game.rooms.entities.types;

import com.cometsrv.game.rooms.avatars.misc.Position;
import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.types.Room;

public class PetEntity extends GenericEntity {
    public PetEntity(Position position, Room room) {
        super(position, room);
    }

    @Override
    public void joinRoom() {

    }

    @Override
    public void leaveRoom() {

    }
}
