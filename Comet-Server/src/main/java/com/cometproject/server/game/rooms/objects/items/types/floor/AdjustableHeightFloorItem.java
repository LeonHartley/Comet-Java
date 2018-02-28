package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import org.apache.commons.lang.StringUtils;


public class AdjustableHeightFloorItem extends RoomItemFloor {
    public AdjustableHeightFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            if (!(entity instanceof PlayerEntity)) {
                return false;
            }

            PlayerEntity pEntity = (PlayerEntity) entity;

            if (!pEntity.getRoom().getRights().hasRights(pEntity.getPlayerId())
                    && !pEntity.getPlayer().getPermissions().getRank().roomFullControl()) {
                return false;
            }
        }

        for (RoomItemFloor floorItem : this.getItemsOnStack()) {
            if (floorItem.getId() != this.getId() && floorItem.getPosition().getZ() >= this.getPosition().getZ())
                return false;
        }

        final double oldHeight = this.getOverrideHeight();

        this.toggleInteract(true);
        this.sendUpdate();

        double newHeight = this.getOverrideHeight();

        for (RoomEntity entityOnItem : this.getEntitiesOnItem()) {
            if (entityOnItem.hasStatus(RoomEntityStatus.SIT)) {
                entityOnItem.removeStatus(RoomEntityStatus.SIT);
            }

            double entityHeight = (newHeight > oldHeight) ? entityOnItem.getPosition().getZ() + (newHeight - oldHeight) : this.getTile().getTileHeight();

            entityOnItem.setPosition(new Position(entityOnItem.getPosition().getX(), entityOnItem.getPosition().getY(), entityHeight));
            this.getRoom().getEntities().broadcastMessage(new AvatarUpdateMessageComposer(entityOnItem));
        }

        this.saveData();
        return true;
    }

    @Override
    public double getOverrideHeight() {
        if (this.getDefinition().getVariableHeights() != null && !this.getItemData().getData().isEmpty()) {
            if (!StringUtils.isNumeric(this.getItemData().getData())) {
                return 0;
            }

            int heightIndex = Integer.parseInt(this.getItemData().getData());

            if (heightIndex >= this.getDefinition().getVariableHeights().length) {
                return 0;
            }

            return this.getDefinition().getVariableHeights()[heightIndex];
        } else if (this.getDefinition().getVariableHeights() != null && this.getDefinition().getVariableHeights().length != 0) {
            return this.getDefinition().getVariableHeights()[0];
        } else {
            if (this.getItemData().getData().isEmpty() || !StringUtils.isNumeric(this.getItemData().getData())) {
                return 0.5;
            } else {
                return Double.parseDouble(this.getItemData().getData());
            }
        }
    }
}
