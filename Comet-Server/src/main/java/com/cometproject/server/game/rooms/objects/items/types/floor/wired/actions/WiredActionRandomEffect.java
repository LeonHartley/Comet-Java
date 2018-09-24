package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.types.Room;
import com.google.common.collect.Lists;
import org.apache.http.impl.conn.Wire;

import java.util.List;


public class WiredActionRandomEffect extends WiredActionItem {
    public WiredActionRandomEffect(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (!(event.entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity playerEntity = ((PlayerEntity) event.entity);

        List<WiredActionItem> actionItems = Lists.newArrayList();

        for(long itemId : this.getWiredData().getSelectedIds()){
            final RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

            for (RoomItemFloor roomItemFloor : this.getRoom().getItems().getItemsOnSquare(floorItem.getPosition().getX(), floorItem.getPosition().getY()))
            {
                if(!(roomItemFloor instanceof WiredActionItem))
                    continue;

                actionItems.add((WiredActionItem) roomItemFloor);
            }
        }

        WiredActionItem actionItem = WiredUtil.getRandomElement(actionItems);

        if (actionItem == null)
            return;

        actionItem.evaluate(playerEntity, null);

    }


    @Override
    public boolean requiresPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 0;
    }
}