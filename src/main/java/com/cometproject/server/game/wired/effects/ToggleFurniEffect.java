package com.cometproject.server.game.wired.effects;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.WiredDataInstance;
import com.cometproject.server.game.wired.types.WiredEffect;
import com.cometproject.server.network.messages.types.Event;

import java.util.List;
import java.util.Random;

public class ToggleFurniEffect extends WiredEffect {
    @Override
    public void onActivate(List<PlayerEntity> entities, FloorItem item) {
        WiredDataInstance data = WiredDataFactory.get(item);

        if(data == null || data.getItems().size() == 0) {
            return;
        }

        Room room = entities.get(0).getRoom();

        if(room == null) {
            return;
        }

        for(int itemId : data.getItems()) {
            FloorItem itemInstance = room.getItems().getFloorItem(itemId);

            if(itemInstance == null)
                return;

            // Toggle furni state
            GameEngine.getItems().getInteractions().onInteract(0, itemInstance, null, true);
        }
    }

    @Override
    public void onSave(Event event, FloorItem item) {
        event.readInt(); // don't need this
        event.readString(); // don't need this

        int itemCount = event.readInt();
        WiredDataInstance instance = WiredDataFactory.get(item);

        if(instance == null) {
            return;
        }

        instance.getItems().clear();

        for(int i = 0; i < itemCount; i++) {
            instance.addItem(event.readInt());
        }

        instance.setDelay(event.readInt());

        WiredDataFactory.save(instance);
    }
}
