package com.cometproject.server.game.wired.effects;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.WiredDataInstance;
import com.cometproject.server.game.wired.types.WiredEffect;
import com.cometproject.server.network.messages.types.Event;

import java.util.Random;

public class ToggleFurniEffect extends WiredEffect {
    private Random random = new Random();
    @Override
    public void onActivate(PlayerEntity avatar, FloorItem item) {
        WiredDataInstance data = WiredDataFactory.get(item);

        if(data.getItems().size() == 0) {
            return;
        }

        for(int itemId : data.getItems()) {
            FloorItem itemInstance = avatar.getRoom().getItems().getFloorItem(itemId);

            if(itemInstance == null)
                return;

            // Toggle furni state
            GameEngine.getItems().getInteractions().onInteract(0, itemInstance, avatar.getPlayer().getEntity(), true);
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
