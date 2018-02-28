package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;


public class OneWayGateFloorItem extends RoomItemFloor {
    private boolean isInUse = false;
    private RoomEntity interactingEntity;

    public OneWayGateFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (this.isInUse) {
            return false;
        }

        if (this.getPosition().squareInFront(this.getRotation()).getX() != entity.getPosition().getX() || this.getPosition().squareInFront(this.getRotation()).getY() != entity.getPosition().getY()) {
            entity.moveTo(this.getPosition().squareInFront(this.getRotation()).getX(), this.getPosition().squareInFront(this.getRotation()).getY());
            return false;
        }

        Position squareBehind = this.getPosition().squareBehind(this.getItemData().getRotation());

        if (!this.getRoom().getMapping().isValidStep(this.getItemData().getPosition(), squareBehind, true)) {
            return false;
        }

        this.isInUse = true;

        entity.setOverriden(true);
        entity.moveTo(squareBehind.getX(), squareBehind.getY());

        this.getItemData().setData("1");
        this.sendUpdate();

        this.interactingEntity = entity;
        this.setTicks(RoomItemFactory.getProcessTime(1.0));

        return true;
    }

    @Override
    public void onTickComplete() {
        if (this.isInUse) {
            this.interactingEntity.setOverriden(false);
            this.setTicks(RoomItemFactory.getProcessTime(1.0));
        }

        this.getItemData().setData("0");
        this.sendUpdate();

        this.isInUse = false;
        this.interactingEntity = null;
    }

    public RoomEntity getInteractingEntity() {
        return interactingEntity;
    }
}
