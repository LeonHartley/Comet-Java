package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredTriggerEnterRoom extends WiredTriggerItem {

    public WiredTriggerEnterRoom(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    public static void executeTriggers(PlayerEntity playerEntity) {
        if (playerEntity == null || playerEntity.getRoom() == null || playerEntity.getRoom().getItems() == null) {
            return;
        }

        for (RoomItemFloor floorItem : getTriggers(playerEntity.getRoom(), WiredTriggerEnterRoom.class)) {
            if (!(floorItem instanceof WiredTriggerEnterRoom)) continue;

            WiredTriggerEnterRoom trigger = ((WiredTriggerEnterRoom) floorItem);

            if (trigger.getWiredData().getText().isEmpty() || trigger.getWiredData().getText().equals(playerEntity.getUsername())) {
                trigger.evaluate(playerEntity, null);
            }
        }
    }

    @Override
    public int getInterface() {
        return 7;
    }

    @Override
    public boolean suppliesPlayer() {
        return true;
    }
}
