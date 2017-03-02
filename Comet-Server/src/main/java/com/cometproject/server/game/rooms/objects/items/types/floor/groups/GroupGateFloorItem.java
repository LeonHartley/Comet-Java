package com.cometproject.server.game.rooms.objects.items.types.floor.groups;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;


public class GroupGateFloorItem extends GroupFloorItem {
    public boolean isOpen = false;

    public GroupGateFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);

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
        this.isOpen = false;
        this.sendUpdate();
    }

    @Override
    public boolean isMovementCancelled(RoomEntity entity) {
        if(!(entity instanceof PlayerEntity)) {
            return true;
        }

        if (!((PlayerEntity) entity).getPlayer().getGroups().contains(this.getGroupId())) {
            return true;
        }

        return false;
    }
}
