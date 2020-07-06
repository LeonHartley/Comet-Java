package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.ItemData;
import com.cometproject.api.game.rooms.objects.data.MapItemData;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

import java.util.HashMap;

public class VideoPlayerFloorItem extends DefaultFloorItem {
    public VideoPlayerFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public ItemData createItemData() {
        return new MapItemData(new HashMap<String, String>() {{
            put("THUMBNAIL_URL", "/deliver/" + getAttribute("video"));
        }});
    }
}
