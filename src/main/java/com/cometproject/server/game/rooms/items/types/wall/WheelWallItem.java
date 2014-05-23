package com.cometproject.server.game.rooms.items.types.wall;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItemFactory;
import com.cometproject.server.game.rooms.items.RoomItemWall;

import java.util.Random;

public class WheelWallItem extends RoomItemWall {
    private boolean isInUse = false;

    private final Random r = new Random();

    public WheelWallItem(int id, int itemId, int roomId, int owner, String position, String data) {
        super(id, itemId, roomId, owner, position, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if (this.isInUse) { return; }

        if (entity instanceof PlayerEntity) {
            PlayerEntity pEntity = (PlayerEntity) entity;
            if (!this.getRoom().getRights().hasRights(pEntity.getPlayerId())) {
                return;
            }
        }

        this.isInUse = true;

        this.setExtraData("-1");
        this.sendUpdate();

        this.setTicks(RoomItemFactory.getProcessTime(4));
    }

    @Override
    public void onTickComplete() {
        int wheelPos = r.nextInt(10) + 1;

        this.setExtraData(Integer.toString(wheelPos));
        this.sendUpdate();

        this.isInUse = false;
    }
}
