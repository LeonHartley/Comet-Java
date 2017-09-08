package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public class BadgeDisplayFloorItem extends DefaultFloorItem {

    public BadgeDisplayFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public void composeItemData(IComposer msg) {
        msg.writeInt(0);
        msg.writeInt(2);
        msg.writeInt(4);

        String badge;
        String name = "";
        String date = "";

        if (this.getExtraData().contains("~")) {
            String[] data = this.getExtraData().split("~");

            badge = data[0];
            name = data[1];
            date = data[2];
        } else {
            badge = this.getExtraData();
        }

        msg.writeString("0");
        msg.writeString(badge);
        msg.writeString(name); // creator
        msg.writeString(date); // date
    }
}
