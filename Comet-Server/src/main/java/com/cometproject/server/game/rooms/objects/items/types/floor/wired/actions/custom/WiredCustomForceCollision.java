package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerCollision;
import com.cometproject.server.game.rooms.types.Room;

public class WiredCustomForceCollision extends WiredActionItem {

    public WiredCustomForceCollision(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 8;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (this.getWiredData().getSelectedIds().size() == 0) return;

        for (long itemId : this.getWiredData().getSelectedIds()) {
            RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

            if (floorItem == null) continue;

            PlayerEntity nearestEntity = floorItem.nearestPlayerEntity();

            if (floorItem.getCollision() != null && this.isCollided(floorItem.getCollision(), floorItem)) {
                WiredTriggerCollision.executeTriggers(nearestEntity, floorItem);
                continue;
            }

            if (nearestEntity != null) {
                if (this.isCollided(nearestEntity, floorItem)) {
                    if (floorItem.getCollision() != nearestEntity) {
                        floorItem.setCollision(nearestEntity);

                        WiredTriggerCollision.executeTriggers(nearestEntity, floorItem);
                    }
                }
            } else {
                return;
            }
        }
    }

    private boolean isCollided(RoomEntity entity, RoomItemFloor floorItem) {
        boolean tilesTouching = entity.getPosition().touching(floorItem.getPosition());

        if (tilesTouching) {
            final boolean xMatches = entity.getPosition().getX() == floorItem.getPosition().getX();
            final boolean yMatches = entity.getPosition().getY() == floorItem.getPosition().getY();

            return xMatches || yMatches;
        }

        return false;
    }
}