package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;


public class WiredActionTeleportPlayer extends WiredActionItem {

    public WiredActionTeleportPlayer(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (event.entity == null) return;

        if (!(event.entity instanceof PlayerEntity) && this.getWiredData() == null || this.getWiredData().getSelectedIds() == null || this.getWiredData().getSelectedIds().isEmpty()) {
            event.entity = null;
            return;
        }

        Long itemId = WiredUtil.getRandomElement(this.getWiredData().getSelectedIds());

        if (itemId == null) {
            event.entity = null;
            return;
        }

        RoomItemFloor item = this.getRoom().getItems().getFloorItem(itemId);

        if (item == null || item.isAtDoor() || item.getPosition() == null || item.getTile() == null) {
            event.entity = null;
            return;
        }

        event.entity.teleportToItem(item);
        event.entity = null;
    }


    @Override
    public int getInterface() {
        return 0;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
