package com.cometsrv.game.rooms.items;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.GameEngine;
import com.cometsrv.game.items.interactions.InteractionAction;
import com.cometsrv.game.items.interactions.InteractionQueueItem;
import com.cometsrv.game.items.types.ItemDefinition;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.outgoing.room.items.UpdateFloorExtraDataMessageComposer;
import com.cometsrv.network.messages.types.Composer;
import com.cometsrv.network.sessions.Session;

import java.lang.ref.WeakReference;
import java.sql.PreparedStatement;

public class FloorItem extends RoomItem {
    private int roomId;
    private double height;
    private String extraData;

    private WeakReference<Room> room;

    public FloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        this.id = id;
        this.itemId = itemId;
        this.roomId = roomId;
        this.ownerId = owner;
        this.x = x;
        this.y = y;
        this.height = z;
        this.rotation = rotation;
        this.extraData = data;

        this.state = false;
    }

    @Override
    public void serialize(Composer msg) {
        msg.writeInt(this.getId());
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeInt(this.getX());
        msg.writeInt(this.getY());
        msg.writeInt(this.getRotation());
        msg.writeString(Double.toString(this.getHeight()));
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeString(this.getExtraData());
        msg.writeInt(-1);
        msg.writeInt(!this.getDefinition().getInteraction().equals("default") ? 1 : 0);
    }

    private ItemDefinition cachedDefinition;

    public ItemDefinition getDefinition() {
        if (cachedDefinition == null) {
            cachedDefinition = GameEngine.getItems().getDefintion(this.getItemId());
        }

        return cachedDefinition;
    }

    @Override
    public boolean toggleInteract(boolean state) {
        String interaction = this.getDefinition().getInteraction();

        if(!state) {
            if(!GameEngine.getWired().isWiredItem(this))
                this.setExtraData("0");

            return true;
        }

        if((interaction.equals("default") || interaction.equals("gate") || interaction.equals("pressure_pad")) && (this.getDefinition().getInteractionCycleCount() > 1)) {
            if(this.getExtraData().isEmpty() || this.getExtraData().equals(" ")) {
                this.setExtraData("0");
            }

            int i = Integer.parseInt(this.getExtraData()) + 1;

            if(i > this.getDefinition().getInteractionCycleCount()) {
                this.setExtraData("0");
            }  else {
                this.setExtraData(i + "");
            }
            return true;
        } else {
            return false;
        }
    }

    @Deprecated
    public boolean handleInteraction(boolean state) {
        return this.toggleInteract(state);
    }

    /*
     * Provides a weak referenced access to the Room this item is placed in
     */
    public Room getRoom() {
        if (this.room == null) {
            this.room = new WeakReference<>(GameEngine.getRooms().get(this.roomId));
        }

        return this.room.get();
    }

    public void saveData() {
        try {
            PreparedStatement statement = Comet.getServer().getStorage().prepare("UPDATE items SET extra_data = ? WHERE id = ?");

            statement.setString(1, this.getExtraData());
            statement.setInt(2, this.getId());

            statement.executeUpdate();
        } catch(Exception e) {
        }
    }

    public void setNeedsUpdate(boolean needsUpdate, InteractionAction action, Avatar avatar, int updateState) {
        if (needsUpdate) {
            this.queueInteraction(new InteractionQueueItem(needsUpdate, this, action, avatar, updateState));
        } else {
            this.setNeedsUpdate(false);
        }
    }

    public void setNeedsUpdate(boolean needsUpdate, InteractionAction action, Avatar avatar, int updateState, int updateCycles) {
        if (needsUpdate) {
            this.queueInteraction(new InteractionQueueItem(needsUpdate, this, action, avatar, updateState, updateCycles));
        } else {
            this.setNeedsUpdate(false);
        }
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        /*this.updateNeeded = needsUpdate;
        this.updateType = null;
        this.updateAvatar = null;
        this.updateState = 0;
        this.updateCycles = 0;*/

        this.curInteractionItem = null;
    }

    @Deprecated
    public void sendUpdate(Session client) {
        //client.getPlayer().getAvatar().getRoom().getAvatars().broadcast(UpdateFloorExtraDataMessageComposer.compose(this.getId(), this.getExtraData()));
        Room r = this.getRoom();

        if (r != null) {
            r.getAvatars().broadcast(UpdateFloorExtraDataMessageComposer.compose(this.getId(), this.getExtraData()));
        }
    }

    public void sendUpdate() {
        //client.getPlayer().getAvatar().getRoom().getAvatars().broadcast(UpdateFloorExtraDataMessageComposer.compose(this.getId(), this.getExtraData()));
        Room r = this.getRoom();

        if (r != null) {
            r.getAvatars().broadcast(UpdateFloorExtraDataMessageComposer.compose(this.getId(), this.getExtraData()));
        }
    }

    public double getHeight() {
        return this.height;
    }

    public String getExtraData() {
        return this.extraData;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRotation(int rot) {
        this.rotation = rot;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setExtraData(String data) {
        this.extraData = data;
    }
}
