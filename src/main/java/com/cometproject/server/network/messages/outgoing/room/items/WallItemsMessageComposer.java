package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class WallItemsMessageComposer extends MessageComposer {
    private final Room room;

    public WallItemsMessageComposer(Room room) {
        this.room = room;
    }

    @Override
    public short getId() {
        return Composers.RoomWallItemsMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        int size = room.getItems().getWallItems().size();

        if (size > 0) {
            msg.writeInt(1);
            msg.writeInt(room.getData().getOwnerId());
            msg.writeString(room.getData().getOwner());
        } else {
            msg.writeInt(0);
        }

        msg.writeInt(size);

        for (RoomItemWall item : room.getItems().getWallItems()) {
            msg.writeString(item.getId());
            msg.writeInt(item.getDefinition().getSpriteId());
            msg.writeString(item.getWallPosition());
            msg.writeString(item.getExtraData());
            msg.writeInt(0);
            msg.writeInt(room.getData().getOwnerId());
            msg.writeInt(0);
        }
    }
}
