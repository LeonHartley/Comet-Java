package com.cometproject.server.game.rooms.objects.entities.types;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.bots.BotData;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.ai.BotAI;
import com.cometproject.server.game.rooms.objects.entities.types.ai.DefaultAI;
import com.cometproject.server.game.rooms.objects.entities.types.ai.MinionAI;
import com.cometproject.server.game.rooms.objects.entities.types.ai.WaiterAI;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class BotEntity extends GenericEntity {
    private BotData data;
    private int cycleCount = 0;
    private BotAI ai;

    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    public BotEntity(BotData data, int identifier, Position startPosition, int startBodyRotation, int startHeadRotation, Room roomInstance) {
        super(identifier, startPosition, startBodyRotation, startHeadRotation, roomInstance);

        this.data = data;

        switch (data.getBotType()) {
            default:
                this.ai = new DefaultAI(this);
                break;

            case "waiter":
                this.ai = new WaiterAI(this);
                break;

            case "mimic":
                this.ai = new MinionAI(this);
                break;
        }
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
        this.getRoom().getEntities().broadcastMessage(new LeaveRoomMessageComposer(this.getId()));

        // Remove entity from the room
        this.getRoom().getEntities().removeEntity(this);
        this.getRoom().getBots().removeBot(this.getUsername());

        this.data.dispose();
        this.data = null;

        this.attributes.clear();
    }

    @Override
    public boolean onChat(String message) {
        return false;
    }

    @Override
    public boolean onRoomDispose() {
        // Send leave room message to all current entities
        this.getRoom().getEntities().broadcastMessage(new LeaveRoomMessageComposer(this.getId()));

        this.data.dispose();
        this.data = null;

        this.attributes.clear();

        return true;
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
    public void compose(IComposer msg) {
        msg.writeInt(this.getBotId());
        msg.writeString(this.getUsername());
        msg.writeString(this.getMotto());
        msg.writeString(this.getFigure());
        msg.writeInt(this.getId());

        msg.writeInt(this.getPosition().getX());
        msg.writeInt(this.getPosition().getY());
        msg.writeDouble(this.getPosition().getZ());
        msg.writeInt(0);
        msg.writeInt(4);

        msg.writeString(this.getGender().toLowerCase());
        msg.writeInt(this.getRoom().getData().getOwnerId());
        msg.writeString(this.getRoom().getData().getOwner());

        msg.writeInt(5);
        msg.writeShort(1);
        msg.writeShort(2);
        msg.writeShort(4);
        msg.writeShort(5);
        msg.writeShort(3);
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

    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {
        if (this.attributes.containsKey(attributeKey)) {
            this.attributes.replace(attributeKey, attributeValue);
        } else {
            this.attributes.put(attributeKey, attributeValue);
        }
    }

    @Override
    public Object getAttribute(String attributeKey) {
        return this.attributes.get(attributeKey);
    }

    @Override
    public boolean hasAttribute(String attributeKey) {
        return this.attributes.containsKey(attributeKey);
    }

    @Override
    public void removeAttribute(String attributeKey) {
        this.attributes.remove(attributeKey);
    }
}
