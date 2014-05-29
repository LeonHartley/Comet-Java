package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFactory;
import com.cometproject.server.game.rooms.items.RoomItemFloor;

import java.util.Random;

public class DiceFloorItem extends RoomItemFloor {
    private boolean isInUse = false;

    public DiceFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if (!this.touching(entity)) {
            entity.moveTo(this.squareInfront().getX(), this.squareInfront().getY());
            return;
        }

        if (this.isInUse) { return; }
        this.isInUse = true;

        if (requestData >= 0) {
            if (!"-1".equals(this.getExtraData())) {
                this.setExtraData("-1");
                this.sendUpdate();

                this.setTicks(RoomItemFactory.getProcessTime(3));
            }
        } else {
            this.setExtraData("0");
            this.sendUpdate();
        }
    }

    @Override
    public void onPlaced() {
        if (!"0".equals(this.getExtraData())) {
            this.setExtraData("0");
        }
    }

    @Override
    public void onPickup() {
        this.cancelTicks();
    }

    @Override
    public void onTickComplete() {
        int num = new Random().nextInt(6) + 1;

        this.setExtraData(Integer.toString(num));
        this.sendUpdate();

        this.isInUse = false;
    }
}
