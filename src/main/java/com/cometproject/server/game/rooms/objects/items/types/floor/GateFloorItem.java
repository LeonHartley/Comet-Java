package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;


public class GateFloorItem extends RoomItemFloor {
    public GateFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
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

        for (AffectedTile tile : AffectedTile.getAffectedTilesAt(this.getDefinition().getLength(), this.getDefinition().getWidth(), this.getPosition().getX(), this.getPosition().getY(), this.getRotation())) {
            if (this.getRoom().getEntities().getEntitiesAt(new Position(tile.x, tile.y)).size() > 0) {
                return;
            }
        }

        if (this.getRoom().getEntities().getEntitiesAt(this.getPosition()).size() > 0) {
            return;
        }

        for (GenericEntity entity : this.getRoom().getEntities().getAllEntities().values()) {
            if (this.getPosition().distanceTo(entity.getPosition()) <= 1 && entity.isWalking()) {
                return;
            }
        }

        this.toggleInteract(true);
        this.sendUpdate();

        // TODO: Move item saving to a queue for batch saving or something. :P
        this.saveData();
    }

    public boolean isOpen() {
        return !this.getExtraData().equals("0");
    }

}
