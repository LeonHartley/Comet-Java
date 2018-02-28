package com.cometproject.server.game.rooms.objects.items.types;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.state.FloorItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.utilities.collections.ConcurrentHashSet;

import java.util.HashSet;
import java.util.Set;

public abstract class AdvancedFloorItem<T extends FloorItemEvent> extends RoomItemFloor {
    private final Set<T> itemEvents = new ConcurrentHashSet<T>();

    public AdvancedFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onTick() {
        final Set<T> finishedEvents = new HashSet<T>();

        for (T itemEvent : itemEvents) {
            itemEvent.incrementTicks();

            if (itemEvent.isFinished()) {
                finishedEvents.add(itemEvent);
            }
        }

        for (T finishedEvent : finishedEvents) {
            this.itemEvents.remove(finishedEvent);

            finishedEvent.onCompletion(this);

            if (finishedEvent.isInteractiveEvent()) {
                this.onEventComplete(finishedEvent);
            }
        }

        finishedEvents.clear();
    }

    public void queueEvent(final T floorItemEvent) {
        if (this.getMaxEvents() <= this.itemEvents.size()) {
            return;
        }

        this.itemEvents.add(floorItemEvent);
    }

    public abstract void onEventComplete(T event);

    public int getMaxEvents() {
        return 5000;
    }
}
