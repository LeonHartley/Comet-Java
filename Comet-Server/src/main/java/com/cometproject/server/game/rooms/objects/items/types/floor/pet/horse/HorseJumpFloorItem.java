package com.cometproject.server.game.rooms.objects.items.types.floor.pet.horse;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;

import java.util.HashSet;
import java.util.Set;

public class HorseJumpFloorItem extends DefaultFloorItem {
    public HorseJumpFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    private Set<RoomEntity> entities = new HashSet<>();

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        final Position[] positions = this.getBarPositions();

        if(!positions[0].equals(entity.getPosition()) && !positions[1].equals(entity.getPosition()) || !this.entities.contains(entity)) {
            if(entity instanceof PetEntity && ((PetEntity) entity).getData().getTypeId() == 15) {
                entity.addStatus(RoomEntityStatus.JUMP, "0");

                this.entities.add(entity);
            }
        } else {
            if(entity instanceof PetEntity && ((PetEntity) entity).getData().getTypeId() == 15) {
                entity.removeStatus(RoomEntityStatus.JUMP);
            }
        }
    }


    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if(entity instanceof PetEntity && ((PetEntity) entity).getData().getTypeId() == 15) {
            entity.removeStatus(RoomEntityStatus.JUMP);

            this.entities.remove(entity);
        }
    }

    @Override
    public void onUnload() {
        this.entities.clear();
    }

    @Override
    public boolean isMovementCancelled(RoomEntity entity) {
        if(entity.getMountedEntity() == null) {
            return true;
        }

        return false;
    }

    private Position[] getBarPositions() {
        Position a = this.getPosition().copy();
        Position b = this.getPosition().copy();

        if(this.rotation == 2) {
            a.setX(a.getX() + 1);
        } else {
            a.setY(a.getY() + 1);
        }

        b.setX(b.getX() + 1);
        b.setY(b.getY() + 1);

        return new Position[] {
            a, b
        };
    }
}
