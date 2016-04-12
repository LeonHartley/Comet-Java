package com.cometproject.server.game.rooms.objects.items.types.floor.groups;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;


public class GroupGateFloorItem extends GroupFloorItem {
    public GroupGateFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        this.setExtraData("0");
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        this.setExtraData("1");
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        this.setTicks(RoomItemFactory.getProcessTime(0.5));
    }

    @Override
    public void onTickComplete() {
        this.setExtraData("0");
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
