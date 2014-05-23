package com.cometproject.server.game.wired.triggers;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.misc.WiredSquare;
import com.cometproject.server.game.wired.types.WiredTrigger;
import com.cometproject.server.network.messages.types.Event;

import java.util.List;

public class OnSayTrigger extends WiredTrigger {

    @Override
    public void onTrigger(Object data, List<GenericEntity> entity, WiredSquare wiredBlock) {
        if (!(data instanceof String))
            return;

        Room room = entity.get(0).getRoom();

        for (RoomItemFloor item : room.getItems().getItemsOnSquare(wiredBlock.getX(), wiredBlock.getY())) {
            // TODO: check for condition
            if (CometManager.getWired().isWiredEffect(item)) {
                CometManager.getWired().getEffect(item.getDefinition().getInteraction()).onActivate(entity, item);
                //item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, null, 0, 1));
            }
        }
    }

    @Override
    public void onSave(Event msg, RoomItemFloor item) {
        msg.readInt();
        msg.readInt(); // 2nd int = isOwner

        String chat = msg.readString();

        item.setExtraData(chat);
        item.saveData();
    }
}
