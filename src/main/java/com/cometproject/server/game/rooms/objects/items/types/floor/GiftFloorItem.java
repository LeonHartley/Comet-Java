package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.RemoveFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SendFloorItemMessageComposer;
import com.cometproject.server.utilities.JsonFactory;


public class GiftFloorItem extends RoomItemFloor {
    private GiftData giftData;
    private boolean isOpened = false;

    public GiftFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        this.giftData = JsonFactory.getInstance().fromJson(data.split("GIFT::##")[1], GiftData.class);
    }

    @Override
    public void onInteract(GenericEntity entity, int state, boolean isWiredTrigger) {
        this.isOpened = true;

        this.getRoom().getEntities().broadcastMessage(RemoveFloorItemMessageComposer.compose(this.getId(), 0));
        this.getRoom().getEntities().broadcastMessage(SendFloorItemMessageComposer.compose(this, this.getRoom()));

        this.isOpened = false;
    }

    public GiftData getGiftData() {
        return giftData;
    }

    public boolean isOpened() {
        return isOpened;
    }
}
