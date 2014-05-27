package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFactory;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.utilities.RandomInteger;

public class VendingMachineFloorItem extends RoomItemFloor {
    private GenericEntity vendingEntity;
    private int state = -1;

    public VendingMachineFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if(!this.touching(entity)) {
            entity.moveTo(this.x, this.y);
            return;
        }

       // if(this.vendingEntity != null) {
       //     return;
       // }

        int rotation = Position3D.calculateRotation(entity.getPosition().getX(), entity.getPosition().getY(), this.getX(), this.getY(), false);

        entity.setBodyRotation(rotation);
        entity.setHeadRotation(rotation);

        entity.markNeedsUpdate();

        this.vendingEntity = entity;

        this.state = 0;
        this.setTicks(RoomItemFactory.getProcessTime(1));
    }

    @Override
    public void onTickComplete() {
        switch (this.state) {
            case 0: {
                this.setExtraData("1");
                this.sendUpdate();

                this.state = 1;
                this.setTicks(RoomItemFactory.getProcessTime(0.5));
                break;
            }

            case 1: {
                int vendingId = Integer.parseInt(this.getDefinition().getVendingIds()[RandomInteger.getRandom(0, this.getDefinition().getVendingIds().length - 1)].trim());
                vendingEntity.carryItem(vendingId);

                this.state = 2;
                this.setTicks(RoomItemFactory.getProcessTime(0.5));
                break;
            }

            case 2: {
                this.setExtraData("0");
                this.sendUpdate();

                this.state = 0;
                this.vendingEntity = null;
                break;
            }
        }
    }

    @Override
    public void onPlaced() {
        this.setExtraData("0");
    }
}
