package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;

public class BackgroundTonerFloorItem extends RoomItemFloor {
    public BackgroundTonerFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        this.setExtraData("");
        this.saveData();
        this.getRoom().getEntities().broadcastMessage(UpdateFloorItemMessageComposer.compose(this, this.getOwner()));
    }
}
