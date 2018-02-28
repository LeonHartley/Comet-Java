package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public class AdsFloorItem extends DefaultFloorItem {
    public AdsFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void composeItemData(IComposer msg) {
        msg.writeInt(0);
        msg.writeInt(1);

        if (!this.getItemData().getData().equals("") && !this.getItemData().getData().equals("0")) {
            String[] adsData = this.getItemData().getData().split(String.valueOf((char) 9));
            int count = adsData.length;

            msg.writeInt(count / 2);

            for (int i = 0; i <= count - 1; i++) {
                msg.writeString(adsData[i]);
            }
        } else {
            msg.writeInt(0);
        }
    }
}
