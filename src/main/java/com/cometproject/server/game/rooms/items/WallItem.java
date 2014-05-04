package com.cometproject.server.game.rooms.items;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateWallItemMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

import java.lang.ref.WeakReference;

public class WallItem extends RoomItem {
    private int roomId;
    private String position;
    private String extraData;
    private boolean state;

    private WeakReference<Room> room;

    public WallItem(int id, int itemId, int roomId, int owner, String position, String data) {
        this.id = id;
        this.itemId = itemId;
        this.roomId = roomId;
        this.ownerId = owner;
        this.position = position;
        this.extraData = data;

        state = false;
    }

    @Override
    public void serialize(Composer msg) {
        this.serialize(msg, false);
    }

    public void serialize(Composer msg, int userId) {
        this.serialize(msg, false, userId);
    }

    public void serialize(Composer msg, boolean isNew) {
        msg.writeString(this.getId());
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeString(this.getPosition());

        msg.writeString(this.getExtraData());
        msg.writeInt(!this.getDefinition().getInteraction().equals("default") ? 1 : 0);
        msg.writeInt(-1);
        msg.writeInt(-1);

        msg.writeInt(this.getRoom().getData().getOwnerId());
    }

    public void serialize(Composer msg, boolean isNew, int userId) {
        msg.writeString(this.getId());
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeString(this.getPosition());

        msg.writeString(this.getExtraData());
        msg.writeInt(!this.getDefinition().getInteraction().equals("default") ? 1 : 0);
        msg.writeInt(-1);
        msg.writeInt(-1);

        msg.writeInt(userId);
    }

    @Override
    public boolean toggleInteract(boolean state) {
        String interaction = this.getDefinition().getInteraction();

        if ((interaction.equals("default") && (this.getDefinition().getInteractionCycleCount() > 1))) {
            if (this.getExtraData().isEmpty() || this.getExtraData().equals(" ")) {
                this.setExtraData("0");
            }

            int i = Integer.parseInt(this.getExtraData()) + 1;

            if (i > (this.getDefinition().getInteractionCycleCount() - 1)) { // take one because count starts at 0 (0, 1) = count(2)
                this.setExtraData("0");
            } else {
                this.setExtraData(i + "");
            }

            return true;
        }

        return false;
    }

    public Room getRoom() {
        if (this.room == null || this.room.get() == null) {
            this.room = new WeakReference<>(GameEngine.getRooms().get(this.roomId));
        }

        return this.room.get();
    }

    @Override
    public void sendUpdate() {
        Room r = this.getRoom();

        if (r != null && r.getEntities() != null) {
            r.getEntities().broadcastMessage(UpdateWallItemMessageComposer.compose(this, this.ownerId, this.getRoom().getData().getOwner()));
        }
    }

    @Override
    public void saveData() {
        RoomItemDao.saveData(id, extraData);
    }

    @Override
    public ItemDefinition getDefinition() {
        return GameEngine.getItems().getDefintion(this.getItemId());
    }

    @Override
    public boolean handleInteraction(boolean state) {
        return this.toggleInteract(state);
    }

    public int getId() {
        return id;
    }

    public int getItemId() {
        return itemId;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getOwner() {
        return ownerId;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return this.position;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String data) {
        this.extraData = data;
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {

    }

    @Override
    public Object getAttribute(String attributeKey) {
        return null;
    }

    @Override
    public boolean hasAttribute(String attributeKey) {
        return false;
    }

    @Override
    public void removeAttribute(String attributeKey) {

    }
}
