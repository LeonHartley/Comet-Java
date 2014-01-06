package com.cometsrv.game.rooms.entities.types;

import com.cometsrv.game.players.types.Player;
import com.cometsrv.game.rooms.avatars.misc.Position3D;
import com.cometsrv.game.rooms.entities.GenericEntity;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.types.Composer;

public class PlayerEntity extends GenericEntity {
    private Player player;

    public PlayerEntity(Player player, int identifier, Position3D startPosition, Room roomInstance) {
        super(identifier, startPosition, roomInstance);

        this.player = player;
    }

    @Override
    public void joinRoom() {

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
        return this.player.getData().getUsername();
    }

    @Override
    public String getMotto() {
        return this.player.getData().getMotto();
    }

    @Override
    public String getFigure() {
        return this.player.getData().getFigure();
    }

    @Override
    public String getGender() {
        return this.player.getData().getGender();
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(this.getId());
        msg.writeString(this.getUsername());
        msg.writeString(this.getMotto());
        msg.writeString(this.getFigure());
        msg.writeInt(this.getId());

        msg.writeInt(this.getPosition().getX());
        msg.writeInt(this.getPosition().getY());
        msg.writeDouble(this.getPosition().getZ());

        msg.writeInt(2); // 2 = user 4 = bot
        msg.writeInt(1); // 1 = user 2 = pet 3 = bot

        msg.writeString(this.getGender().toLowerCase());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeString("");
        msg.writeString("");
        msg.writeInt(0);
    }
}
