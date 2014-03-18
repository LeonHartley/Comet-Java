package com.cometproject.server.game.rooms.entities.types;

import com.cometproject.server.game.bots.BotData;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.ai.BotAI;
import com.cometproject.server.game.rooms.entities.types.ai.DefaultAI;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.types.Composer;

public class PetEntity extends GenericEntity {
    private BotData data;
    private BotAI ai;

    public PetEntity(BotData data, int identifier, Position3D startPosition, int startBodyRotation, int startHeadRotation, Room roomInstance) {
        super(identifier, startPosition, startBodyRotation, startHeadRotation, roomInstance);

        this.data = data;
        this.ai = new DefaultAI(this);
    }

    @Override
    public void joinRoom(Room room, String password) {

    }

    @Override
    protected void finalizeJoinRoom() {

    }

    @Override
    public void leaveRoom(boolean isOffline, boolean isKick, boolean toHotelView) {

    }

    @Override
    protected void finalizeLeaveRoom() {

    }

    @Override
    public boolean onChat(String message) {
        return false;
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
