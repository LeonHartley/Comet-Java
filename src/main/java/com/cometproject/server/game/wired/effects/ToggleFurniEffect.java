package com.cometproject.server.game.wired.effects;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.WiredDataInstance;
import com.cometproject.server.game.wired.types.WiredEffect;
import com.cometproject.server.network.messages.types.Event;

import java.util.List;

public class ToggleFurniEffect extends WiredEffect {
    @Override
    public void onActivate(List<GenericEntity> entities, RoomItemFloor item) {
        WiredDataInstance data = WiredDataFactory.get(item);

        if (data == null || data.getItems().size() == 0) {
            return;
        }

        Room room = entities.get(0).getRoom();

        if (room == null) {
            return;
        }

        for (int itemId : data.getItems()) {
            RoomItemFloor itemInstance = room.getItems().getFloorItem(itemId);

            if (itemInstance == null)
                return;

            // Toggle furni state
            //CometManager.getItems()//.getInteractions().onInteract(0, itemInstance, null, true);
            itemInstance.onInteract(null, 0, true);
        }
    }

    @Override
    public void onSave(Event event, RoomItemFloor item) {
        event.readInt(); // don't need this
        event.readString(); // don't need this

        int itemCount = event.readInt();
        WiredDataInstance instance = WiredDataFactory.get(item);

        if (instance == null) {
            return;
        }

        instance.getItems().clear();

        for (int i = 0; i < itemCount; i++) {
            instance.addItem(event.readInt());
        }

        instance.setDelay(event.readInt());

        WiredDataFactory.save(instance);
    }
}
