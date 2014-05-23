package com.cometproject.server.game.wired.triggers;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.wired.misc.WiredSquare;
import com.cometproject.server.game.wired.types.WiredTrigger;
import com.cometproject.server.network.messages.types.Event;

import java.util.List;

public class EnterRoomTrigger extends WiredTrigger {
    @Override
    public void onTrigger(Object data, List<GenericEntity> entities, WiredSquare wiredBlock) {

        for (RoomItemFloor item : entities.get(0).getRoom().getItems().getItemsOnSquare(wiredBlock.getX(), wiredBlock.getY())) {
            // TODO: check for condition.
            if (CometManager.getWired().isWiredEffect(item)) {
                CometManager.getWired().getEffect(item.getDefinition().getInteraction()).onActivate(entities, item);
                //item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, null, 0, 0));
            }
        }
    }

    @Override
    public void onSave(Event event, RoomItemFloor item) {
        int mode = event.readInt();
        String username = event.readString();

        item.setExtraData(username);
        item.saveData();
    }
}
