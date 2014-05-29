package com.cometproject.server.game.rooms.items.types;

import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItemWall;

public final class GenericWallItem extends RoomItemWall {
    public GenericWallItem(int id, int itemId, int roomId, int owner, String position, String data) {
        super(id, itemId, roomId, owner, position, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            if (!(entity instanceof PlayerEntity))
                return;

            if (!entity.getRoom().getRights().hasRights(((PlayerEntity) entity).getPlayerId())) {
                return;
            }
        }

        this.toggleInteract(true);
        this.sendUpdate();

        // TODO: Move item saving to a queue for batch saving or something. :P
        this.saveData();
    }
}
