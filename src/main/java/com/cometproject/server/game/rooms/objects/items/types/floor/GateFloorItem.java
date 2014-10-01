package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.entities.misc.Position3D;
import com.cometproject.server.game.rooms.entities.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;

public class GateFloorItem extends RoomItemFloor {
    public GateFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity0, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            if (!(entity0 instanceof PlayerEntity)) {
                return;
            }

            PlayerEntity pEntity = (PlayerEntity) entity0;

            if (!pEntity.getRoom().getRights().hasRights(pEntity.getPlayerId())
                    && !pEntity.getPlayer().getPermissions().hasPermission("room_full_control")) {
                return;
            }
        }

        for (AffectedTile tile : AffectedTile.getAffectedTilesAt(this.getDefinition().getLength(), this.getDefinition().getWidth(), this.getX(), this.getY(), this.getRotation())) {
            if (this.getRoom().getEntities().getEntitiesAt(tile.x, tile.y).size() > 0) {
                return;
            }
        }

        if (this.getRoom().getEntities().getEntitiesAt(this.getX(), this.getY()).size() > 0) {
            return;
        }

        for (GenericEntity entity : this.getRoom().getEntities().getEntitiesCollection().values()) {
            if (Position3D.distanceBetween(entity.getPosition(), new Position3D(this.getX(), this.getY(), 0d)) <= 1 && entity.isWalking()) {
                return;
            }
        }

        this.toggleInteract(true);
        this.sendUpdate();

        // TODO: Move item saving to a queue for batch saving or something. :P
        this.saveData();
    }
}
