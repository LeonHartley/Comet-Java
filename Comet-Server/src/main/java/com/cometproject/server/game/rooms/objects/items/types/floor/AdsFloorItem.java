package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.ItemData;
import com.cometproject.api.game.rooms.objects.data.MapItemData;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

import java.util.HashMap;

public class AdsFloorItem extends DefaultFloorItem {
    public AdsFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public ItemData createItemData() {
        final HashMap<String, String> data = new HashMap<>();

        if (!this.getItemData().getData().equals("") && !this.getItemData().getData().equals("0")) {
            String[] adsData = this.getItemData().getData().split(String.valueOf((char) 9));
            int count = adsData.length;

            String key = null;
            for (int i = 0; i <= count - 1; i++) {
                if (key == null) {
                    key = adsData[i];
                } else {
                    data.put(key, adsData[i]);
                }
            }

        }

        return new MapItemData(data);
    }
}
