package com.cometproject.server.game.items.interactions.items;

import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.utilities.RandomInteger;

public class VendingMachineInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar, boolean isWiredTriggered) {
        if(isWiredTriggered)
            return false;

        if (!item.touching(avatar)) {
            avatar.moveTo(item.squareInfront().getX(), item.squareInfront().getY());
            return false;
        }

        if(item.getDefinition().getVendingIds().length < 1)
            return false;

        int vendingId = Integer.parseInt(item.getDefinition().getVendingIds()[RandomInteger.getRandom(0, item.getDefinition().getVendingIds().length - 1)].trim());

        // TODO: item animation
        item.toggleInteract(true);

        avatar.carryItem(vendingId);
        return true;
    }

    @Override
    public boolean onPlace(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
