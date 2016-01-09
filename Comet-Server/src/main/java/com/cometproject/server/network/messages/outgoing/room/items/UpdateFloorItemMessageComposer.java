package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class UpdateFloorItemMessageComposer extends MessageComposer {
    private final RoomItemFloor item;

    public UpdateFloorItemMessageComposer(RoomItemFloor item) {
        this.item = item;
    }

    @Override
    public short getId() {
        return Composers.ObjectUpdateMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        this.item.serialize(msg);
        msg.writeInt(this.item.getOwner());
    }
}
