package com.cometproject.server.game.rooms.objects.items.types.wall;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;

import java.util.Random;


public class WheelWallItem extends RoomItemWall {
    private final Random r = new Random();
    private boolean isInUse = false;

    public WheelWallItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (this.isInUse) {
            return false;
        }

        if (entity instanceof PlayerEntity) {
            PlayerEntity pEntity = (PlayerEntity) entity;
            if (!this.getRoom().getRights().hasRights(pEntity.getPlayerId())) {
                return false;
            }
        }

        this.isInUse = true;

        this.getItemData().setData("-1");
        this.sendUpdate();

        this.setTicks(RoomItemFactory.getProcessTime(4));
        return true;
    }

    @Override
    public void onTickComplete() {
        int wheelPos = r.nextInt(10) + 1;

        this.getItemData().setData(Integer.toString(wheelPos));
        this.sendUpdate();

        this.isInUse = false;
    }

    @Override
    public void onPickup() {
        this.cancelTicks();
    }

    @Override
    public void onPlaced() {
        if (!"0".equals(this.getItemData().getData())) {
            this.getItemData().setData("0");
        }
    }
}
