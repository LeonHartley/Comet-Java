package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;


public class OneWayGateFloorItem extends RoomItemFloor {
    private boolean isInUse = false;
    private RoomEntity interactingEntity;

    public OneWayGateFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
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

        Position squareBehind = this.getPosition().squareBehind(this.rotation);

        if (!this.getRoom().getMapping().isValidStep(this.getPosition(), squareBehind, true)) {
            return false;
        }

        this.isInUse = true;

        entity.setOverriden(true);
        entity.moveTo(squareBehind.getX(), squareBehind.getY());

        this.setExtraData("1");
        this.sendUpdate();

        this.interactingEntity = entity;
        this.setTicks(RoomItemFactory.getProcessTime(2.0));


        return true;
    }

    @Override
    public void onTickComplete() {
        this.interactingEntity.setOverriden(false);

        this.setExtraData("0");
        this.sendUpdate();

        this.isInUse = false;
        this.interactingEntity = null;
    }
}
