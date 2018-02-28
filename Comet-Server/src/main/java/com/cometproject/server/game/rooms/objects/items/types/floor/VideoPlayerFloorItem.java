package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public class VideoPlayerFloorItem extends DefaultFloorItem {
    public VideoPlayerFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void composeItemData(IComposer msg) {
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(1);
        msg.writeString("THUMBNAIL_URL");
        msg.writeString("/deliver/" + this.getAttribute("video"));
    }
}
