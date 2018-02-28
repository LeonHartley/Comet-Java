package com.cometproject.server.game.rooms.objects.items.types.floor.wired.base;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.wired.dialog.WiredConditionMessageComposer;
import com.cometproject.server.protocol.messages.MessageComposer;


public abstract class WiredConditionItem extends WiredFloorItem {
    protected boolean isNegative;

    public WiredConditionItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        this.isNegative = this.getClass().getSimpleName().startsWith("WiredNegativeCondition");
    }

    @Override
    public MessageComposer getDialog() {
        return new WiredConditionMessageComposer(this);
    }
}
