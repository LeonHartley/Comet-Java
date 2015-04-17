package com.cometproject.server.game.rooms.objects.items.types.floor.groups;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorExtraDataMessageComposer;


public class GroupGateFloorItem extends GroupFloorItem {
    public GroupGateFloorItem(int id, int itemId, RoomInstance room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityPreStepOn(GenericEntity entity) {
        this.setExtraData("1");
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {
        this.setTicks(RoomItemFactory.getProcessTime(0.5));
    }

    @Override
    public void onTickComplete() {
        this.setExtraData("0");
        this.sendUpdate();
    }

    @Override
    public void sendUpdate() {
        RoomInstance r = this.getRoom();

        if (r != null) {
            r.getEntities().broadcastMessage(new UpdateFloorExtraDataMessageComposer(this.getId(), this, false));
        }
    }
}
