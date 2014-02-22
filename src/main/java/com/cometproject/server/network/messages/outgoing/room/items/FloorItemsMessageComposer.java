package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.WallItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class FloorItemsMessageComposer {
    public static Composer compose(Room room) {
        Composer msg = new Composer(Composers.FloorItemsMessageComposer);

        msg.writeInt(1);
        msg.writeInt(room.getData().getOwnerId());
        msg.writeString(room.getData().getOwner());
        msg.writeInt(room.getItems().getFloorItems().size());

        for(FloorItem item : room.getItems().getFloorItems()) {
            item.serialize(msg);
            msg.writeInt(room.getData().getOwnerId());
        }

        msg.writeInt(room.getItems().getWallItems().size());

        for(WallItem item : room.getItems().getWallItems()) {
            msg.writeString(item.getId());
            msg.writeInt(item.getDefinition().getSpriteId());
            msg.writeString(item.getPosition());
            msg.writeString(item.getExtraData());
            msg.writeInt(0);
            msg.writeInt(room.getData().getOwnerId());
            msg.writeInt(0);
        }

        return msg;
    }
}
