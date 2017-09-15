package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public class VideoPlayerFloorItem extends DefaultFloorItem {
    public VideoPlayerFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
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
