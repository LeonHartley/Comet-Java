package com.cometproject.server.game.rooms.items.queue;

import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.items.RoomItemWall;
import javolution.util.FastTable;

/**
 * Processes item events asynchronously without blocking the user threads
 */
public class RoomItemEventQueue {
    private final FastTable<RoomItemEventQueueEntry> eventQueue = new FastTable<>();
    private final Object lock = new Object();

    public void cycle() {
        if (this.eventQueue.size() == 0) { return; }
        final FastTable<RoomItemEventQueueEntry> eventQueueCopy = new FastTable<>();

        synchronized (this.lock) { // reduce lock time by copying (events happen outside lock)
            for (RoomItemEventQueueEntry e : this.eventQueue) {
                eventQueueCopy.add(e);
            }

            this.eventQueue.clear();
        }

        for (RoomItemEventQueueEntry e : eventQueueCopy) {
            if (e.getItem() instanceof RoomItemWall) {
                RoomItemWall wall = (RoomItemWall) e.getItem();

                switch (e.getType()) {
                    case Pickup:
                        wall.onPickup();
                        break;

                    case Placed:
                        wall.onPlaced();
                        return;

                    case Interact:
                        wall.onInteract(e.getEntity(), e.getRequestData(), e.isWiredTrigger());
                        break;
                }
            } else if (e.getItem() instanceof RoomItemFloor) {
                RoomItemFloor floor = (RoomItemFloor) e.getItem();

                switch (e.getType()) {
                    case Pickup:
                        floor.onPickup();
                        break;

                    case Placed:
                        floor.onPlaced();
                        return;

                    case Interact:
                        floor.onInteract(e.getEntity(), e.getRequestData(), e.isWiredTrigger());
                        break;

                    case PreStepOn:
                        floor.onEntityPreStepOn(e.getEntity());
                        break;

                    case StepOn:
                        floor.onEntityStepOn(e.getEntity());
                        break;

                    case StepOff:
                        floor.onEntityStepOff(e.getEntity());
                        break;
                }
            }
        }

        eventQueueCopy.clear();
    }

    // TO-DO: avoid synchronizing, i am being lazy i already did this in the 'chat log processing code'
    public void queue(RoomItemEventQueueEntry entry) {
        synchronized (this.lock) {
            this.eventQueue.add(entry);
        }
    }
}
