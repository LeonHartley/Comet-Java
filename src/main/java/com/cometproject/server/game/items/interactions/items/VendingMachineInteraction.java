package com.cometproject.server.game.items.interactions.items;

import com.cometproject.server.game.items.interactions.InteractionAction;
import com.cometproject.server.game.items.interactions.InteractionManager;
import com.cometproject.server.game.items.interactions.InteractionQueueItem;
import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.utilities.DistanceCalculator;
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

        int rotation = Position3D.calculateRotation(avatar.getPosition().getX(), avatar.getPosition().getY(), item.getX(), item.getY(), false);

        avatar.setBodyRotation(rotation);
        avatar.setHeadRotation(rotation);

        avatar.markNeedsUpdate();

        item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 0, 2));
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
    public boolean onTick(RoomItem item, PlayerEntity avatar, int updateState) {
        switch(updateState) {
            case 0:
                item.setExtraData("1");
                item.sendUpdate();

                item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 1, 2));
                break;

            case 1:
                int vendingId = Integer.parseInt(item.getDefinition().getVendingIds()[RandomInteger.getRandom(0, item.getDefinition().getVendingIds().length - 1)].trim());
                avatar.carryItem(vendingId);

                item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 2, 7));
                break;

            case 2:
                item.setExtraData("0");
                item.sendUpdate();

                item.saveData();
                break;
        }
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
