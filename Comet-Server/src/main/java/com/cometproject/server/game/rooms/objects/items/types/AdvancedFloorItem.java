package com.cometproject.server.game.rooms.objects.items.types;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.state.FloorItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.tasks.CometThreadManager;
import com.cometproject.server.utilities.collections.ConcurrentHashSet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AdvancedFloorItem<T extends FloorItemEvent> extends RoomItemFloor {
    private final Set<T> itemEvents = new ConcurrentHashSet<T>();

    public AdvancedFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public void onTick() {
        final Set<T> finishedEvents = new HashSet<T>();

        for(T itemEvent : itemEvents) {
            itemEvent.incrementTicks();

            if(itemEvent.isFinished()) {
                finishedEvents.add(itemEvent);
            }
        }

        for(T finishedEvent : finishedEvents) {
            this.itemEvents.remove(finishedEvent);

            CometThreadManager.getInstance().executeOnce(() -> finishedEvent.onCompletion(this));

            if (finishedEvent.isInteractiveEvent()) {
                CometThreadManager.getInstance().executeOnce(() -> this.onEventComplete(finishedEvent));
            }
        }

        finishedEvents.clear();
    }

    public void queueEvent(final T floorItemEvent) {
        if(this.getMaxEvents() <= this.itemEvents.size()) {
            return;
        }

        this.itemEvents.add(floorItemEvent);
    }

    public abstract void onEventComplete(T event);

    public int getMaxEvents() {
        return 5000;
    }
}
