package com.cometsrv.game.rooms.items;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.GameEngine;
import com.cometsrv.game.items.interactions.InteractionAction;
import com.cometsrv.game.items.types.ItemDefinition;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.network.messages.outgoing.room.items.UpdateFloorExtraDataMessageComposer;
import com.cometsrv.network.messages.types.Composer;
import com.cometsrv.network.sessions.Session;

import java.sql.PreparedStatement;

public class FloorItem {
    private int id;
    private int itemId;
    private int owner;
    private int x;
    private int y;
    private int rotation;
    private float height;

    private String extraData;
    private boolean state;

    private InteractionAction updateType;
    private boolean updateNeeded;
    private Avatar updateAvatar;
    private int updateState;
    private int updateCycles;

    public int interactingAvatar;
    public int interactingAvatar2;

    public FloorItem(int id, int itemId, int owner, int x, int y, float z, int rotation, String data) {
        this.id = id;
        this.itemId = itemId;
        this.owner = owner;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.height = z;
        this.extraData = data;

        state = false;
    }

    public void serialize(Composer msg) {
        msg.writeInt(this.getId());
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeInt(this.getX());
        msg.writeInt(this.getY());
        msg.writeInt(this.getRotation());
        msg.writeString(Float.toString(this.getHeight()));
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeString(this.getExtraData());
        msg.writeInt(-1);
        msg.writeInt(!this.getDefinition().getInteraction().equals("default") ? 1 : 0);
    }

    public boolean handleInteraction(boolean state) {
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

    public boolean needsUpdate() {
        return (this.updateType != null && updateNeeded && updateAvatar != null);
    }

    public void setNeedsUpdate(boolean needsUpdate, InteractionAction action, Avatar avatar, int updateState) {
        this.updateNeeded = needsUpdate;
        this.updateType = action;
        this.updateAvatar = avatar;
        this.updateState = updateState;

        if (action == InteractionAction.ON_TICK) {
            this.updateCycles = 1; // default
        }
    }

    public void setNeedsUpdate(boolean needsUpdate, InteractionAction action, Avatar avatar, int updateState, int updateCycles) {
        this.updateNeeded = needsUpdate;
        this.updateType = action;
        this.updateAvatar = avatar;
        this.updateState = updateState;
        this.updateCycles = updateCycles;

        if (action != InteractionAction.ON_TICK) {
            this.updateCycles = 0;
        }
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        this.updateNeeded = needsUpdate;
        this.updateType = null;
        this.updateAvatar = null;
        this.updateState = 0;
    }

    public void decrementUpdateCycles() {
        this.updateCycles--;

        if (this.updateCycles < 0) {
            this.updateCycles = 0;
        }
    }

    public int getUpdateCycles() {
        return this.updateCycles;
    }

    public Avatar getUpdateAvatar() {
        return this.updateAvatar;
    }

    public int getUpdateState() {
        return this.updateState;
    }

    public InteractionAction getUpdateType() {
        return this.updateType;
    }

    public void sendUpdate(Session client) {
        client.getPlayer().getAvatar().getRoom().getAvatars().broadcast(UpdateFloorExtraDataMessageComposer.compose(this.getId(), this.getExtraData()));
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

    private ItemDefinition cachedDefinition;
    private long definitionLastUpdated;

    public ItemDefinition getDefinition() {
        if (cachedDefinition == null) {
            cachedDefinition = GameEngine.getItems().getDefintion(this.getItemId());
            //definitionLastUpdated = System.nanoTime();
        }

        return cachedDefinition;
    }

    public int getId() {
        return id;
    }

    public int getItemId() {
        return itemId;
    }

    public int getOwner() {
        return owner;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRotation() {
        return rotation;
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

    public float getHeight() {
        return height;
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

    public void toggleState() {
        if(state) {
            state = false;
        } else {
            state = true;
        }
    }
}
