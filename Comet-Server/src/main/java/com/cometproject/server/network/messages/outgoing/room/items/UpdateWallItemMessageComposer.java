package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.objects.items.types.wall.PostItWallItem;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


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
        return Composers.ItemUpdateMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

        String extradata = (item instanceof PostItWallItem ? item.getItemData().getData().split(" ")[0] : item.getItemData().getData());

        msg.writeString(item.getVirtualId());
        msg.writeInt(item.getDefinition().getSpriteId());
        msg.writeString(item.getWallPosition());
        msg.writeString(extradata);
        msg.writeInt(!item.getDefinition().getInteraction().equals("default") ? 1 : 0);
        msg.writeInt(ownerId);
        msg.writeString(owner);
    }
}
