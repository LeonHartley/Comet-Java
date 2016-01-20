package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.catalog.CatalogManager;
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

    public GiftFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) throws Exception {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        this.giftData = JsonFactory.getInstance().fromJson(data.split("GIFT::##")[1], GiftData.class);

        if(!CatalogManager.getInstance().getGiftBoxesNew().contains(giftData.getSpriteId()) && !CatalogManager.getInstance().getGiftBoxesOld().contains(giftData.getSpriteId())) {
            throw new Exception("some sad fucker used an exploit, bye bye gift.");
        }
    }

    @Override
    public boolean onInteract(GenericEntity entity, int state, boolean isWiredTrigger) {
        this.isOpened = true;

        this.getRoom().getEntities().broadcastMessage(new RemoveFloorItemMessageComposer(this.getVirtualId(), 0));
        this.getRoom().getEntities().broadcastMessage(new SendFloorItemMessageComposer(this));

        this.isOpened = false;
        return true;
    }

    public GiftData getGiftData() {
        return giftData;
    }

    public boolean isOpened() {
        return isOpened;
    }
}
