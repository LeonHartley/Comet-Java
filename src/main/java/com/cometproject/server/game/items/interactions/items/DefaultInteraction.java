package com.cometproject.server.game.items.interactions.items;

import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.avatars.effects.UserEffect;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;

public class DefaultInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        if (item.getDefinition().getEffectId() != 0) {
            if (avatar.getCurrentEffect() == null) {
                avatar.applyEffect(new UserEffect(item.getDefinition().getEffectId(), true));
            }
        }
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar, boolean isWiredTriggered) {
        if (!isWiredTriggered && !avatar.getRoom().getRights().hasRights(avatar.getPlayerId()))
            return false;

        item.toggleInteract(true);
        item.sendUpdate();
        item.saveData();

        return false;
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
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
