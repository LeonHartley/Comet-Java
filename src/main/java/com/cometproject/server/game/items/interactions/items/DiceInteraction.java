package com.cometproject.server.game.items.interactions.items;

import com.cometproject.server.game.items.interactions.InteractionAction;
import com.cometproject.server.game.items.interactions.InteractionManager;
import com.cometproject.server.game.items.interactions.InteractionQueueItem;
import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;

import java.util.Random;

public class DiceInteraction extends Interactor {

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
        if(!isWiredTriggered) {
            if (!item.touching(avatar)) {
                avatar.moveTo(item.getX(), item.getY());
                return false;
            }
        }

        if (request >= 0) {
            if (!"-1".equals(item.getExtraData())) {
                item.setExtraData("-1");
                item.sendUpdate();

                //item.setNeedsUpdate(true, InteractionAction.ON_TICK, avatar, 0, InteractionManager.DICE_ROLL_TIME);
                item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 0, InteractionManager.DICE_ROLL_TIME));
            }
        } else {
            item.setExtraData("0");
            item.sendUpdate();
        }

        return false;
    }

    @Override
    public boolean onPlace(RoomItem item, PlayerEntity avatar, Room room) {
        if (!"0".equals(item.getExtraData())) {
            item.setExtraData("0");
            item.sendUpdate();
        }

        return false;
    }

    @Override
    public boolean onPickup(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(RoomItem item, PlayerEntity avatar, int updateState) {
        int num = new Random().nextInt(6) + 1;

        // Provide a bit more randomness
        if (Integer.toString(num).equals(item.getExtraData())) {
            if (num < 2) {
                num++;
            } else {
                num--;
            }
        }

        item.setExtraData(Integer.toString(num));
        item.sendUpdate();

        return true;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
