package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class OneWayGateFloorItem extends RoomItemFloor {
    private boolean isInUse = false;
    private GenericEntity interactingEntity;

    public OneWayGateFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if (this.isInUse) {
            return;
        }
        this.isInUse = true;

        Position doorPosition = new Position(this.getPosition().getX(), this.getPosition().getY());

        if (doorPosition.squareInFront(this.getRotation()).getX() != entity.getPosition().getX() && doorPosition.squareInFront(this.getRotation()).getY() != entity.getPosition().getY()) {
            entity.moveTo(doorPosition.squareInFront(this.getRotation()).getX(), doorPosition.squareInFront(this.getRotation()).getY());
            return;
        }

        entity.setOverriden(true);
        entity.moveTo(doorPosition.squareBehind(this.getRotation()).getX(), doorPosition.squareBehind(this.getRotation()).getY());

        this.setExtraData("1");
        this.sendUpdate();

        this.interactingEntity = entity;
        this.setTicks(14);
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
