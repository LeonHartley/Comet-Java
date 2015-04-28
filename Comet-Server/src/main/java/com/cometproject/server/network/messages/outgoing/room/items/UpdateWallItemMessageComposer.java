package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class UpdateWallItemMessageComposer extends MessageComposer {
    private final RoomItemWall item;
    private final int ownerId;
    private final String owner;

    public UpdateWallItemMessageComposer(RoomItemWall item, int ownerId, String owner) {
        this.item = item;
        this.owner = owner;
        this.ownerId = ownerId;
    }

    @Override
    public short getId() {
        return Composers.UpdateRoomWallItemMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(item.getId());
        msg.writeInt(item.getDefinition().getSpriteId());
        msg.writeString(item.getWallPosition());
        msg.writeString(item.getExtraData());
        msg.writeInt(!item.getDefinition().getInteraction().equals("default") ? 1 : 0);
        msg.writeInt(ownerId);
        msg.writeString(owner);
    }
}
