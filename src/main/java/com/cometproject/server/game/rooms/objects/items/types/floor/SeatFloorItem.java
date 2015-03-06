package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;


public class SeatFloorItem extends RoomItemFloor {

    public SeatFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            if (!(entity instanceof PlayerEntity)) {
                return false;
            }

            PlayerEntity pEntity = (PlayerEntity) entity;

            if (!pEntity.getRoom().getRights().hasRights(pEntity.getPlayerId())
                    && !pEntity.getPlayer().getPermissions().hasPermission("room_full_control")) {
                return false;
            }
        }

        this.toggleInteract(true);
        this.sendUpdate();

        if (this instanceof AdjustableHeightSeatFloorItem) {
            for (GenericEntity sitter : this.getEntitiesOnItem()) {
                this.onEntityStepOn(sitter, true);
            }
        }

        // TODO: Move item saving to a queue for batch saving or something. :P
        this.saveData();
        return true;
    }

    public void onEntityStepOn(GenericEntity entity, boolean instantUpdate) {
        double height = (entity instanceof PetEntity || entity.hasAttribute("transformation")) ? this.getSitHeight() / 2 : this.getSitHeight();

        entity.setBodyRotation(this.getRotation());
        entity.setHeadRotation(this.getRotation());
        entity.addStatus(RoomEntityStatus.SIT, String.valueOf(height).replace(',', '.'));

        if (instantUpdate)
            this.getRoom().getEntities().broadcastMessage(new AvatarUpdateMessageComposer(entity));
        else
            entity.markNeedsUpdate();
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        this.onEntityStepOn(entity, false);
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {
        if (entity.hasStatus(RoomEntityStatus.SIT)) {
            entity.removeStatus(RoomEntityStatus.SIT);
        }

        entity.markNeedsUpdate();
    }

    public double getSitHeight() {
        return this.getDefinition().getHeight();
    }
}
