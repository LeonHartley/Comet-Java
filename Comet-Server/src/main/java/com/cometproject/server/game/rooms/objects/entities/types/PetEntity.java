package com.cometproject.server.game.rooms.objects.entities.types;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.ai.BotAI;
import com.cometproject.server.game.rooms.objects.entities.types.ai.pets.PetAI;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.LeaveRoomMessageComposer;
import com.cometproject.server.storage.queries.pets.PetDao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class PetEntity extends RoomEntity {
    private PetData data;
    private PetAI ai;

    private int cycleCount = 0;

    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    public PetEntity(PetData data, int identifier, Position startPosition, int startBodyRotation, int startHeadRotation, Room roomInstance) {
        super(identifier, startPosition, startBodyRotation, startHeadRotation, roomInstance);

        this.data = data;
        this.ai = new PetAI(this);
    }

    @Override
    public void joinRoom(Room room, String password) {

    }

    @Override
    protected void finalizeJoinRoom() {

    }

    @Override
    public void leaveRoom(boolean isOffline, boolean isKick, boolean toHotelView) {
        this.leaveRoom(false);
    }

    public void leaveRoom(boolean save) {
        if (save) {
            PetDao.savePosition(this.getPosition().getX(), this.getPosition().getY(), this.data.getId());
        }

        this.getRoom().getEntities().removeEntity(this);
        this.getRoom().getEntities().broadcastMessage(new LeaveRoomMessageComposer(this.getId()));
        this.attributes.clear();
    }

    @Override
    public boolean onChat(String message) {
        return false;
    }

    @Override
    public boolean onRoomDispose() {
//        PetDao.savePosition(this.getPosition().getX(), this.getPosition().getY(), this.data.getId());

        this.getRoom().getEntities().broadcastMessage(new LeaveRoomMessageComposer(this.getId()));

        this.attributes.clear();
        return true;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getMotto() {
        return null;
    }

    @Override
    public String getFigure() {
        return null;
    }

    @Override
    public String getGender() {
        return null;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.data.getId());
        msg.writeString(this.data.getName());
        msg.writeString("PET_MOTTO");

        String composer = data.getLook().toLowerCase() + " ";

        if (this.getData().getTypeId() == 15) {
            composer += new StringBuilder().append(this.getData().isSaddled() ? "3" : "2").append(" 2 ").append(this.getData().getHair()).append(" ").append(this.getData().getHairDye()).append(" 3 ").append(this.getData().getHair()).append(" ").append(this.getData().getHairDye()).append(this.getData().isSaddled() ? "0 4 9 0" : "").toString();
        } else {
            composer += "2 2 -1 0 3 -1 0";
        }

        msg.writeString(composer);
        msg.writeInt(this.getId());

        msg.writeInt(this.getPosition().getX());
        msg.writeInt(this.getPosition().getY());
        msg.writeDouble(this.getPosition().getZ());

        msg.writeInt(0); // 2 = user 4 = bot 0 = pet ??????
        msg.writeInt(2); // 1 = user 2 = pet 3 = bot ??????

        msg.writeInt(this.data.getRaceId());

        msg.writeInt(this.data.getOwnerId());
        msg.writeString("Leon"); // TODO: this :P
        msg.writeInt(1);
        msg.writeBoolean(true); // has saddle
        msg.writeBoolean(this.hasMount()); // has rider?

        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeString("");
    }

    public PetData getData() {
        return data;
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

    @Override
    public BotAI getAI() {
        return ai;
    }

    public PetAI getPetAI() {
        return this.ai;
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
