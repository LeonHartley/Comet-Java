package com.cometproject.server.game.rooms.entities.types;

import com.cometproject.server.game.bots.BotData;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.ai.BotAI;
import com.cometproject.server.game.rooms.entities.types.ai.DefaultAI;
import com.cometproject.server.game.rooms.entities.types.data.PlayerBotData;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.components.types.Trade;
import com.cometproject.server.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.messages.types.Composer;

public class BotEntity extends GenericEntity {
    private BotData data;
    private int cycleCount = 0;
    private BotAI ai;

    public BotEntity(BotData data, int identifier, Position3D startPosition, int startBodyRotation, int startHeadRotation, Room roomInstance) {
        super(identifier, startPosition, startBodyRotation, startHeadRotation, roomInstance);

        this.data = data;

        // Currently only the default ai is supported - more will be added soon maybe
        this.ai = new DefaultAI(this);
    }

    @Override
    public void joinRoom(Room room, String password) {

    }

    @Override
    protected void finalizeJoinRoom() {

    }

    public void leaveRoom() {
        this.leaveRoom(false, false, false);
    }

    @Override
    public void leaveRoom(boolean isOffline, boolean isKick, boolean toHotelView) {
        // Send leave room message to all current entities
        this.getRoom().getEntities().broadcastMessage(LeaveRoomMessageComposer.compose(this.getVirtualId()));

        // Remove entity from the room
        this.getRoom().getEntities().removeEntity(this);
    }

    @Override
    public void finalizeLeaveRoom() {

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

    public int getBotId() {
        return this.data.getId();
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(this.getBotId());
        msg.writeString(this.getUsername());
        msg.writeString(this.getMotto());
        msg.writeString(this.getFigure());
        msg.writeInt(this.getVirtualId());//vid

        msg.writeInt(this.getPosition().getX());
        msg.writeInt(this.getPosition().getY());
        msg.writeDouble(this.getPosition().getZ());
        msg.writeInt(0);
        msg.writeInt(4);

        msg.writeString(this.getGender().toLowerCase());
        msg.writeInt(this.getRoom().getData().getOwnerId());
        msg.writeString(this.getRoom().getData().getOwner());

        msg.writeInt(4);
        msg.writeShort(1);
        msg.writeShort(2);
        msg.writeShort(5);
        msg.writeShort(4);
    }

    public BotData getData() {
        return this.data;
    }

    public int getCycleCount() {
        return this.cycleCount;
    }

    public void decrementCycleCount() {
        cycleCount--;
    }

    public void incrementCycleCount() {
        cycleCount++;
    }

    public void resetCycleCount() {
        this.cycleCount = 0;
    }

    public BotAI getAI() {
        return ai;
    }
}
