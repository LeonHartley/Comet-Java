package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

import java.util.Random;


public class DiceFloorItem extends RoomItemFloor {
    private boolean isInUse = false;
    private int rigNumber = -1;

    public DiceFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            if (!this.getPosition().touching(entity.getPosition())) {
                entity.moveTo(this.getPosition().squareInFront(this.getRotation()).getX(), this.getPosition().squareBehind(this.getRotation()).getY());
                return false;
            }
        }

        if (this.isInUse) {
            return false;
        }

        if (requestData >= 0) {
            if (!"-1".equals(this.getItemData().getData())) {
                this.getItemData().setData("-1");
                this.sendUpdate();

                this.isInUse = true;

                if (entity != null) {
                    if (entity.hasAttribute("diceRoll")) {
                        this.rigNumber = (int) entity.getAttribute("diceRoll");
                        entity.removeAttribute("diceRoll");
                    }
                }

                this.setTicks(RoomItemFactory.getProcessTime(2.5));
            }
        } else {
            this.getItemData().setData("0");
            this.sendUpdate();

            this.saveData();
        }

        return true;
    }

    @Override
    public void onPlaced() {
        if (!"0".equals(this.getItemData().getData())) {
            this.getItemData().setData("0");
        }
    }

    @Override
    public void onPickup() {
        this.cancelTicks();
    }

    @Override
    public void onTickComplete() {
        int num = new Random().nextInt(6) + 1;

        this.getItemData().setData(Integer.toString(this.rigNumber == -1 ? num : this.rigNumber));
        this.sendUpdate();

        this.saveData();

        if (this.rigNumber != -1) this.rigNumber = -1;

        this.isInUse = false;
    }
}
