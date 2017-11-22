package com.cometproject.server.game.rooms.objects.items.types.floor.wired.base;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.data.WiredActionItemData;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.protocol.messages.MessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.wired.dialog.WiredActionMessageComposer;
import com.google.common.collect.Lists;

import java.util.List;


public abstract class WiredActionItem extends WiredFloorItem {
    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredActionItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public MessageComposer getDialog() {
        return new WiredActionMessageComposer(this);
    }

    @Override
    public final boolean evaluate(RoomEntity entity, Object data) {
        if (this.hasTicks()) return false;

        final WiredItemEvent itemEvent = new WiredItemEvent(entity, data);

        if (this.getWiredData().getDelay() >= 1) {
            itemEvent.setTotalTicks(RoomItemFactory.getProcessTime(this.getWiredData().getDelay() / 2));

            this.queueEvent(itemEvent);
        } else {
            itemEvent.onCompletion(this);
            this.onEventComplete(itemEvent);
        }

        return true;
    }

    @Override
    public WiredActionItemData getWiredData() {
        return (WiredActionItemData) super.getWiredData();
    }

    public List<WiredTriggerItem> getIncompatibleTriggers() {
        List<WiredTriggerItem> incompatibleTriggers = Lists.newArrayList();

        if (this.requiresPlayer()) {
            for (RoomItemFloor floorItem : this.getItemsOnStack()) {
                if (floorItem instanceof WiredTriggerItem) {
                    if (!((WiredTriggerItem) floorItem).suppliesPlayer()) {
                        incompatibleTriggers.add(((WiredTriggerItem) floorItem));
                    }
                }
            }
        }

        return incompatibleTriggers;
    }

    public abstract boolean requiresPlayer();
}
