package com.cometproject.server.game.wired.triggers;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.WiredDataInstance;
import com.cometproject.server.game.wired.misc.WiredSquare;
import com.cometproject.server.game.wired.types.WiredTrigger;
import com.cometproject.server.network.messages.types.Event;

import java.util.List;

public class TimerTrigger extends WiredTrigger {
    @Override
    public void onTrigger(Object data, List<PlayerEntity> entities, WiredSquare wiredBlock) {
        if(entities.size() == 0) {
            return;
        }

        Room room = entities.get(0).getRoom();

        for(FloorItem item : room.getItems().getItemsOnSquare(wiredBlock.getX(), wiredBlock.getY())) {
            // TODO: check for condition
            if(GameEngine.getWired().isWiredEffect(item)) {
                try {
                    GameEngine.getWired().getEffect(item.getDefinition().getInteraction()).onActivate(entities, item);
                } catch(Exception ignored) {

                }
            }
        }
    }

    @Override
    public void onSave(Event event, FloorItem item) {
        event.readInt();

        WiredDataInstance instance = WiredDataFactory.get(item);

        if(instance == null) {
            return;
        }

        instance.setDelay(event.readInt());

        WiredDataFactory.save(instance);
    }
}
