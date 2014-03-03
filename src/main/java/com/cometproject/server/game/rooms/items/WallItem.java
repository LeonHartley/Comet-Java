package com.cometproject.server.game.rooms.items;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorExtraDataMessageComposer;
import com.cometproject.server.network.messages.types.Composer;

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
        msg.writeString(this.getId());
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeString(this.position);

        msg.writeString(this.getExtraData());
        msg.writeInt(!this.getDefinition().getInteraction().equals("default") ? 1 : 0);
        msg.writeInt(-1);
        msg.writeInt(-1);
    }

    @Override
    public boolean toggleInteract(boolean state) {
        return false;
    }

    public Room getRoom() {
        if (this.room == null) {
            this.room = new WeakReference<>(GameEngine.getRooms().get(this.roomId));
        }

        return this.room.get();
    }

    @Override
    public void sendUpdate() {
        Room r = this.getRoom();

        if (r != null) {
            r.getEntities().broadcastMessage(UpdateFloorExtraDataMessageComposer.compose(this.getId(), this.getExtraData()));

            // TODO: Check this..
        }
    }

    @Override
    public void saveData() {

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

    public int getRoomId() { return roomId; }

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
}
