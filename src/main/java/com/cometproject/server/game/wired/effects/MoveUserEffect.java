package com.cometproject.server.game.wired.effects;

import com.cometproject.server.game.rooms.avatars.effects.UserEffect;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.effects.TeleportToItemData;
import com.cometproject.server.game.wired.types.WiredEffect;
import com.cometproject.server.network.messages.types.Event;

import java.util.Random;


public class MoveUserEffect extends WiredEffect {
    private Random randomGenerator = new Random();

    @Override
    public void onActivate(PlayerEntity avatar, FloorItem item) {
        TeleportToItemData data = (TeleportToItemData) WiredDataFactory.get(item);

        if(data.getItems().size() == 0) {
            return;
        }

        int locationItemId = data.getItems().get(randomGenerator.nextInt(data.getItems().size()));

        FloorItem itemInstance = avatar.getRoom().getItems().getFloorItem(locationItemId);

        if(itemInstance == null)
            return;

        Position3D position = new Position3D(itemInstance.getX(), itemInstance.getY(), itemInstance.getHeight());

        //if(!avatar.getRoom().getMapping().isValidStep(avatar.getPosition(), position, true)) {
        //    return;
        //}

        // Teleport player to position
        avatar.applyEffect(new UserEffect(4, 5));
        avatar.updateAndSetPosition(position);

        avatar.markNeedsUpdate();
    }

    @Override
    public void onSave(Event event, FloorItem item) {
        event.readInt(); // don't need this
        event.readString(); // don't need this

        int itemCount = event.readInt();
        TeleportToItemData instance = (TeleportToItemData) WiredDataFactory.get(item);

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
