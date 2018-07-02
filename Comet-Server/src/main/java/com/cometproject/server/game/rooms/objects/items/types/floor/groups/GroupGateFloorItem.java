package com.cometproject.server.game.rooms.objects.items.types.floor.groups;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.types.Room;


public class GroupGateFloorItem extends GroupFloorItem {
    public boolean isOpen = false;

    public GroupGateFloorItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        this.isOpen = false;
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        this.isOpen = true;
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        this.setTicks(RoomItemFactory.getProcessTime(0.5));
    }

    @Override
    public void onTickComplete() {
        if (this.getTile().getEntities().size() != 0) {
            return;
        }

        this.isOpen = false;
        this.sendUpdate();
    }

    @Override
    public boolean isMovementCancelled(RoomEntity entity) {
        if (!(entity instanceof PlayerEntity)) {
            return true;
        }

        final PlayerEntity playerEntity = (PlayerEntity) entity;


        return !playerEntity.getPlayer().getGroups().contains(this.getGroupId()) && playerEntity.getPlayer().getData().getRank() < 4;

    }
}
