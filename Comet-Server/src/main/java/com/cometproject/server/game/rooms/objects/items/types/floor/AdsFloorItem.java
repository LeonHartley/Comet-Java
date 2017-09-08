package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public class AdsFloorItem extends DefaultFloorItem {
    public AdsFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public void composeItemData(IComposer msg) {
        msg.writeInt(0);
        msg.writeInt(1);

        if (!this.getExtraData().equals("") && !this.getExtraData().equals("0")) {
            String[] adsData = this.getExtraData().split(String.valueOf((char) 9));
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
