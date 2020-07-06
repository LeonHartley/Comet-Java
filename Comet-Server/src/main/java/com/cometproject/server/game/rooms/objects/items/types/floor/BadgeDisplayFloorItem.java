package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.ItemData;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.rooms.objects.data.StringArrayItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public class BadgeDisplayFloorItem extends DefaultFloorItem {

    public BadgeDisplayFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        return super.onInteract(entity, requestData, isWiredTrigger);
    }

    @Override
    public ItemData createItemData() {
        String badge;
        String name = "";
        String date = "";

        if (this.getItemData().getData().contains("~")) {
            String[] data = this.getItemData().getData().split("~");

            badge = data[0];
            name = data[1];
            date = data[2];
        } else {
            badge = this.getItemData().getData();
        }

        return new StringArrayItemData(new String[] { "0", badge, name, date });
    }

}
