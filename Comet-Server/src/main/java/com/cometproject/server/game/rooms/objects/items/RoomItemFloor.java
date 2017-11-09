package com.cometproject.server.game.rooms.objects.items;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.*;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredFloorItem;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.cometproject.server.storage.queue.types.ItemStorageQueue;
import com.cometproject.server.utilities.attributes.Collidable;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.util.List;


public abstract class RoomItemFloor extends RoomItem implements Collidable {
    private String extraData;

    private ItemDefinition itemDefinition;
    private RoomEntity collidedEntity;
    private boolean hasQueuedSave;
    private String coreState;
    private boolean stateSwitched = false;

    public RoomItemFloor(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, new Position(x, y, z), room);

        this.itemId = itemId;
        this.ownerId = owner;
        this.ownerName = ownerName;
        this.rotation = rotation;
        this.extraData = data;
    }

    public void serialize(IComposer msg, boolean isNew) {
        //final boolean isGift = false;

        /*if (this.giftData != n    ull) {
            isGift = true;
        }*/

        // TODO: MOVE SPECIAL PROPERTIES TO THE INDIVIDUAL ITEM CLASS!!!
        // This needs cleaning up immensely.

        msg.writeInt(this.getVirtualId());
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeInt(this.getPosition().getX());
        msg.writeInt(this.getPosition().getY());
        msg.writeInt(this.getRotation());

        msg.writeString(this instanceof MagicStackFloorItem ? this.getExtraData() : this.getPosition().getZ());

        final double walkHeight = this instanceof AdjustableHeightFloorItem ? this.getOverrideHeight() : this.getDefinition().getHeight();
        msg.writeString(walkHeight);//this.getDefinition().canWalk() ? walkHeight : "0");

        if (this.getLimitedEditionItemData() != null) {
            msg.writeInt(0);
            msg.writeString("");
            msg.writeBoolean(true);
            msg.writeBoolean(false);
            msg.writeString(this.getExtraData());

            msg.writeInt(this.getLimitedEditionItemData().getLimitedRare());
            msg.writeInt(this.getLimitedEditionItemData().getLimitedRareTotal());
        } else {
            this.composeItemData(msg);
        }

        msg.writeInt(-1);
        //msg.writeInt(!this.getDefinition().getInteraction().equals("default") ? 1 : 0);
        msg.writeInt(!(this instanceof DefaultFloorItem) && !(this instanceof SoundMachineFloorItem) ? 1 : 0);
        msg.writeInt(this.ownerId);

        if (isNew)
            msg.writeString(this.ownerName);
    }

    @Override
    public void serialize(IComposer msg) {
        this.serialize(msg, false);
    }

    public ItemDefinition getDefinition() {
        if (this.itemDefinition == null) {
            this.itemDefinition = ItemManager.getInstance().getDefinition(this.getItemId());
        }

        return this.itemDefinition;
    }

    public void onItemAddedToStack(RoomItemFloor floorItem) {
        // override me
    }

    public void onEntityPreStepOn(RoomEntity entity) {
        // override me
    }

    public void onEntityStepOn(RoomEntity entity) {
        // override me
    }

    public void onEntityPostStepOn(RoomEntity entity) {
        // override me
    }

    public void onEntityStepOff(RoomEntity entity) {
        // override me
    }

    public void onPositionChanged(Position newPosition) {
        // override me
    }

    public boolean isMovementCancelled(RoomEntity entity) {
        return false;
    }

    public boolean isMovementCancelled(RoomEntity entity, Position position) {
        return this.isMovementCancelled(entity);
    }

    @Override
    public boolean toggleInteract(boolean state) {
        if (!state) {
            if (!(this instanceof WiredFloorItem))
                this.setExtraData("0");

            return true;
        }

        if (!StringUtils.isNumeric(this.getExtraData())) {
            return true;
        }

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

    @Override
    public void save() {
        if (CometSettings.storageItemQueueEnabled) {
            ItemStorageQueue.getInstance().queueSave(this);
        } else {
            RoomItemDao.saveItem(this);
            this.hasQueuedSave = true;
        }
    }

    @Override
    public void saveData() {
        if (CometSettings.storageItemQueueEnabled) {
            ItemStorageQueue.getInstance().queueSaveData(this);
        } else {
            RoomItemDao.saveData(this.getId(), this.getDataObject());
        }
    }

    @Override
    public void sendUpdate() {
        Room r = this.getRoom();

        if (r != null) {
            r.getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(this));
        }
    }

    public void tempState(int state) {
        this.stateSwitched = true;
        this.coreState = this.extraData;
        this.setExtraData(state);
        this.sendUpdate();
    }

    public void restoreState() {
        this.stateSwitched = false;

        this.setExtraData(coreState);
        this.sendUpdate();
    }

    public List<RoomItemFloor> getItemsOnStack() {
        List<RoomItemFloor> floorItems = Lists.newArrayList();

        List<AffectedTile> affectedTiles = AffectedTile.getAffectedTilesAt(
                this.getDefinition().getLength(), this.getDefinition().getWidth(), this.getPosition().getX(), this.getPosition().getY(), this.getRotation());

        floorItems.addAll(this.getRoom().getItems().getItemsOnSquare(this.getPosition().getX(), this.getPosition().getY()));

        for (AffectedTile tile : affectedTiles) {
            for (RoomItemFloor floorItem : this.getRoom().getItems().getItemsOnSquare(tile.x, tile.y)) {
                if (!floorItems.contains(floorItem)) floorItems.add(floorItem);
            }
        }

        return floorItems;
    }

    public List<RoomEntity> getEntitiesOnItem() {
        List<RoomEntity> entities = Lists.newArrayList();

        entities.addAll(this.getRoom().getEntities().getEntitiesAt(this.getPosition()));

        for (AffectedTile affectedTile : AffectedTile.getAffectedTilesAt(this.getDefinition().getLength(), this.getDefinition().getWidth(), this.getPosition().getX(), this.getPosition().getY(), this.getRotation())) {
            List<RoomEntity> entitiesOnTile = this.getRoom().getEntities().getEntitiesAt(new Position(affectedTile.x, affectedTile.y));

            entities.addAll(entitiesOnTile);
        }

        return entities;
    }

    public Position getPartnerTile() {
        if (this.getDefinition().getLength() != 2) return null;

        for (AffectedTile affTile : AffectedTile.getAffectedBothTilesAt(this.getDefinition().getLength(), this.getDefinition().getWidth(), this.getPosition().getX(), this.getPosition().getY(), this.getRotation())) {
            if (affTile.x == this.getPosition().getX() && affTile.y == this.getPosition().getY()) continue;

            return new Position(affTile.x, affTile.y);
        }

        return null;
    }

    public RoomEntity getCollision() {
        return this.collidedEntity;
    }

    public void setCollision(RoomEntity entity) {
        this.collidedEntity = entity;
    }

    public void nullifyCollision() {
        this.collidedEntity = null;
    }

    public double getOverrideHeight() {
        return -1d;
    }

    public String getDataObject() {
        return this.extraData;
    }

    public String getExtraData() {
        return this.extraData;
    }

    public void setExtraData(String data) {
        this.extraData = data;
    }

    public void setExtraData(Integer i) {
        this.extraData = "" + i;
    }

    public void setRotation(int rot) {
        this.rotation = rot;
    }

    public boolean hasQueuedSave() {
        return hasQueuedSave;
    }

    public void setHasQueuedSave(boolean hasQueuedSave) {
        this.hasQueuedSave = hasQueuedSave;
    }

    public boolean isStateSwitched() {
        return stateSwitched;
    }

    public void setStateSwitched(boolean stateSwitched) {
        this.stateSwitched = stateSwitched;
    }
}
