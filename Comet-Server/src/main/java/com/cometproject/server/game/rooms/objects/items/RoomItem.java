package com.cometproject.server.game.rooms.objects.items;

import com.cometproject.api.game.furniture.types.FurnitureDefinition;
import com.cometproject.api.game.rooms.objects.IRoomItemData;
import com.cometproject.api.game.rooms.objects.data.LimitedEditionItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.items.types.LowPriorityItemProcessor;
import com.cometproject.server.game.rooms.objects.BigRoomFloorObject;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.types.AdvancedFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.RollerFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.SoundMachineFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.football.FootballGateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.games.banzai.BanzaiTeleporterFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.utilities.attributes.Attributable;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public abstract class RoomItem extends BigRoomFloorObject implements Attributable {
    private final Set<Long> wiredItems = Sets.newHashSet();
    private final IRoomItemData itemData;
    protected int ticksTimer;
    private LimitedEditionItemData limitedEditionItemData;
    private Map<String, Object> attributes;
    private int moveDirection = -1;

    public RoomItem(IRoomItemData roomItemData, Room room) {
        super(roomItemData.getId(), roomItemData.getPosition(), room);

        this.itemData = roomItemData;
        this.ticksTimer = -1;
    }

    public boolean toggleInteract(boolean state) {
        if (!state) {
            if (!(this instanceof WiredFloorItem))
                this.getItemData().setData("0");

            return true;
        }

        if (!StringUtils.isNumeric(this.getItemData().getData())) {
            return true;
        }

        if (this.getDefinition().getInteractionCycleCount() > 1) {
            if (this.getItemData().getData().isEmpty() || this.getItemData().getData().equals(" ")) {
                this.getItemData().setData("0");
            }

            int i = Integer.parseInt(this.getItemData().getData()) + 1;

            if (i > (this.getDefinition().getInteractionCycleCount() - 1)) { // take one because count starts at 0 (0, 1) = count(2)
                this.getItemData().setData("0");
            } else {
                this.getItemData().setData(i + "");
            }

            return true;
        } else {
            return false;
        }
    }


    public Set<Long> getWiredItems() {
        return this.wiredItems;
    }

    public final boolean requiresTick() {
        return this.hasTicks() || this instanceof WiredFloorItem || this instanceof AdvancedFloorItem || this instanceof RollerFloorItem;
    }

    protected final boolean hasTicks() {
        return (this.ticksTimer > 0);
    }

    protected final void setTicks(int time) {
        this.ticksTimer = time;

        if (this instanceof BanzaiTeleporterFloorItem) {
            LowPriorityItemProcessor.getInstance().submit(((RoomItemFloor) this));
        }
    }

    protected final void cancelTicks() {
        this.ticksTimer = -1;
    }

    public final void tick() {
        this.onTick();

        if (this.ticksTimer > 0) {
            this.ticksTimer--;
        }

        if (this.ticksTimer == 0) {
            this.cancelTicks();
            this.onTickComplete();
        }
    }

    protected void onTick() {
        // Override this
    }

    protected void onTickComplete() {
        // Override this
    }

    public void onPlaced() {
        // Override this
    }

    public void onPickup() {
        // Override this
    }

    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        // Override this
        return true;
    }

    public void onLoad() {
        // Override this
    }

    public void onUnload() {
        // Override this
    }

    public void onEntityLeaveRoom(RoomEntity entity) {
        // Override this
    }

    public void composeItemData(IComposer msg) {
        msg.writeInt(1);
        msg.writeInt(0);

        msg.writeString((this instanceof FootballGateFloorItem) ? "" :
                (this instanceof WiredFloorItem) ? ((WiredFloorItem) this).getState() ? "1" : "0" :
                        (this instanceof SoundMachineFloorItem) ? ((SoundMachineFloorItem) this).getState() ? "1" : "0" :
                                this.getItemData().getData());
    }

    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }

        if (this.attributes.containsKey(attributeKey)) {
            this.attributes.replace(attributeKey, attributeValue);
        } else {
            this.attributes.put(attributeKey, attributeValue);
        }
    }

    @Override
    public Object getAttribute(String attributeKey) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }

        return this.attributes.get(attributeKey);
    }

    @Override
    public boolean hasAttribute(String attributeKey) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }

        return this.attributes.containsKey(attributeKey);
    }

    @Override
    public void removeAttribute(String attributeKey) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }

        this.attributes.remove(attributeKey);
    }

    public abstract void serialize(IComposer msg);

    public abstract FurnitureDefinition getDefinition();

    public abstract void sendUpdate();

    public abstract void save();

    public abstract void saveData();

    public void dispose() {

    }

    public IRoomItemData getItemData() {
        return this.itemData;
    }

    public LimitedEditionItemData getLimitedEditionItemData() {
        return limitedEditionItemData;
    }

    public void setLimitedEditionItemData(LimitedEditionItemData limitedEditionItemData) {
        this.limitedEditionItemData = limitedEditionItemData;
    }

    public int getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(int moveDirection) {
        this.moveDirection = moveDirection;
    }

    public abstract int getRotation();
}
