package com.cometsrv.game.rooms.entities.types;

import com.cometsrv.game.bots.BotData;
import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.types.Composer;

public class PetEntity extends GenericEntity {
    private BotData data;

    public PetEntity(BotData data, int identifier, Position3D startPosition, Room roomInstance) {
        super(identifier, startPosition, roomInstance);

        this.data = data;
    }

    @Override
    public void joinRoom(Room room, String password) {

    }

    @Override
    protected void finalizeJoinRoom() {

    }

    @Override
    public void leaveRoom() {

    }

    @Override
    public void onChat(String message) {

    }

    @Override
    public String getUsername() {
        return this.data.getUsername();
    }

    @Override
    public String getMotto() {
        return this.data.getMotto();
    }

    @Override
    public String getFigure() {
        return this.data.getFigure();
    }

    @Override
    public String getGender() {
        return this.data.getGender();
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(this.getVirtualId());
        msg.writeString(this.getUsername());
        msg.writeString(this.getMotto());
        msg.writeString(this.getFigure());
        msg.writeInt(this.getVirtualId());

        msg.writeInt(this.getPosition().getX());
        msg.writeInt(this.getPosition().getY());
        msg.writeDouble(this.getPosition().getZ());

        msg.writeInt(4); // 2 = user 4 = bot
        msg.writeInt(2); // 1 = user 2 = pet 3 = bot

        msg.writeString(this.getGender().toLowerCase());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeString("");
        msg.writeString("");
        msg.writeInt(0);
    }
}
