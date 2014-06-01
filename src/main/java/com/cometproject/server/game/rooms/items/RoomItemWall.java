package com.cometproject.server.game.rooms.items;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateWallItemMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

public abstract class RoomItemWall extends RoomItem {
    private int roomId;
    private String position;
    private String extraData;

    public RoomItemWall(int id, int itemId, int roomId, int owner, String position, String data) {
        this.id = id;
        this.itemId = itemId;
        this.roomId = roomId;
        this.ownerId = owner;
        this.position = position;
        this.extraData = data;
    }

    @Override
    public void serialize(Composer msg) {
        msg.writeString(this.getId());
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeString(this.getPosition());

        msg.writeString(this.getExtraData());
        msg.writeInt(!this.getDefinition().getInteraction().equals("default") ? 1 : 0);
        msg.writeInt(-1);
        msg.writeInt(-1);

        //msg.writeInt(this.getRoom().getData().getOwnerId());
        msg.writeInt(1);
    }

    @Override
    public boolean toggleInteract(boolean state) {
        if (this.getDefinition().getInteractionCycleCount() > 1) {
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
        } else {
            return false;
        }
    }

    public Room getRoom() {
        return CometManager.getRooms().get(this.roomId);
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
        return CometManager.getItems().getDefintionNullable(this.getItemId());
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    public int getRoomId() {
        return roomId;
    }

    @Override
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
}
