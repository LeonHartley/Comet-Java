package com.cometproject.server.game.rooms.objects.items;

import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.objects.RoomObject;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.utilities.DistanceCalculator;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.utilities.attributes.Attributable;

import java.util.HashMap;
import java.util.Map;

public abstract class RoomItem extends RoomObject implements Attributable {
    protected int itemId;
    protected int ownerId;

    protected int rotation;

    protected int ticksTimer;

    private final Map<String, Object> attributes = new HashMap<>();

    public int getItemId() {
        return this.itemId;
    }

    public int getOwner() {
        return this.ownerId;
    }

    public int getRotation() {
        return this.rotation;
    }

    public RoomItem(int id, Position position, Room room) {
        super(id, position, room);
        this.ticksTimer = -1;
    }

    public final boolean requiresTick() {
        return this.hasTicks();
    }

    protected final boolean hasTicks() {
        return (this.ticksTimer > 0);
    }

    protected final void setTicks(int time) {
        this.ticksTimer = time;
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

    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        // Override this
    }

    public void onLoad() {
        // Override this
    }

    public void onUnload() {
        // Override this
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

    public abstract void serialize(Composer msg);

    public abstract ItemDefinition getDefinition();

    public abstract boolean toggleInteract(boolean state);

    public abstract void sendUpdate();

    public abstract void saveData();

    public abstract String getExtraData();

    public abstract void setExtraData(String data);

    public void dispose() {

    }
}
