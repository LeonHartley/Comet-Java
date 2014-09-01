package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;

public class SeatFloorItem extends RoomItemFloor {

    public SeatFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            if (!(entity instanceof PlayerEntity)) {
                return;
            }

            PlayerEntity pEntity = (PlayerEntity) entity;

            if (!pEntity.getRoom().getRights().hasRights(pEntity.getPlayerId())
                    && !pEntity.getPlayer().getPermissions().hasPermission("room_full_control")) {
                return;
            }
        }

        this.toggleInteract(true);
        this.sendUpdate();

        // TODO: Move item saving to a queue for batch saving or something. :P
        this.saveData();
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        double height = (entity instanceof PetEntity || entity.hasAttribute("transformation")) ? this.getDefinition().getHeight() / 2 : this.getDefinition().getHeight();

        entity.setBodyRotation(this.getRotation());
        entity.setHeadRotation(this.getRotation());
        entity.addStatus("sit", String.valueOf(height).replace(',', '.'));
        entity.markNeedsUpdate();
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {
        if (entity.hasStatus("sit")) {
            entity.removeStatus("sit");
        }

        entity.markNeedsUpdate();
    }
}
