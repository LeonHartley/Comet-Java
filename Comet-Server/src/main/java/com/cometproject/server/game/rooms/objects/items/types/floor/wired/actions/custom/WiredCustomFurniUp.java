package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import org.apache.commons.lang.StringUtils;

public class WiredCustomFurniUp extends WiredActionItem {

    protected boolean isUp;

    public WiredCustomFurniUp(RoomItemData itemData, Room room) {
        super(itemData, room);
        this.isUp = true;
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 22;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (this.getWiredData() == null || this.getWiredData().getText() == null) {
            return;
        }

        if (!StringUtils.isNumeric(this.getWiredData().getText())) {
            return;
        }

        Integer newZ = 0;

        try {
            newZ = Integer.parseInt(this.getWiredData().getText());
        } catch (NumberFormatException ignored){}

        for (long itemId : this.getWiredData().getSelectedIds()) {
            final RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

            if (floorItem == null)
                continue;

            if(!this.isUp && floorItem.getPosition().getZ() - newZ < 0)
                continue;

            Position newPos = new Position(floorItem.getPosition().getX(), floorItem.getPosition().getY(), this.isUp ? floorItem.getPosition().getZ() + newZ : floorItem.getPosition().getZ() - newZ);

            this.getRoom().getItems().moveFloorItem(floorItem.getId(), newPos, floorItem.getRotation(), true, false, null);
            this.getRoom().getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(floorItem));
            floorItem.sendUpdate();
        }
    }
}
