package com.cometproject.server.game.rooms.objects.items.events.types;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.events.AbstractItemEvent;

public class TeleportItemEvent extends AbstractItemEvent {
    private int stage = 0;

    public TeleportItemEvent(RoomItemFloor floorItem, RoomEntity entity) {
        super(floorItem, entity);
    }

    @Override
    public void run() {
        switch (this.stage) {
            default:
            case 0: {
                this.stage = 1;
                this.runIn(1.5);
                break;
            }

            case 1: {
                System.out.println("Stage 1!");
                break;
            }
        }
    }
}
