package com.cometproject.server.game.rooms.objects.items.types.floor.pet.horse;

import com.cometproject.server.game.pets.races.PetType;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import com.cometproject.server.utilities.RandomUtil;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class HorseJumpFloorItem extends DefaultFloorItem {
    public HorseJumpFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);

        this.setExtraData("0");
    }

    private boolean restore = false;

    private PetEntity petEntity;

    @Override
    public void onTickComplete() {
        if (this.restore) {
            this.restore = false;

            this.setExtraData("0");
            this.sendUpdate();
            return;
        }

        this.setExtraData("0");

        if(this.petEntity.getData().getHappiness() == 0) {
            this.petEntity.getPetAI().applyGesture("sad");
            this.setExtraData("3");
        } else if(this.petEntity.getData().getHunger() == 0) {
            this.petEntity.getPetAI().applyGesture("hng");
            this.setExtraData("3");
        } else if(this.petEntity.getData().getEnergy() == 0) {
            this.petEntity.getPetAI().applyGesture("nrg");
            this.setExtraData("3");
        }

        if(this.getExtraData().equals("0")) {
            final int random = RandomUtil.getRandomInt(1, 100);

            if (random >= 66) {
                this.setExtraData("4");
            } else if (random <= 50) {
                this.setExtraData("2");
            } else if (random <= 10) {
                this.setExtraData("1");
            } else {
                this.setExtraData("3");
            }
        }

        this.sendUpdate();

        if (this.getExtraData().equals("4")) {
            // success!
            this.petEntity.getPetAI().increaseExperience(5);
            this.petEntity.getPetAI().applyGesture("sml");
        } else {
            // failure!
            this.petEntity.getPetAI().applyGesture("sad");
        }

        this.restore = true;
        this.setTicks(RoomItemFactory.getProcessTime(1.5));
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (!(entity instanceof PetEntity)) {
            return;
        }

        final PetEntity petEntity = (PetEntity) entity;
        final Position[] positions = this.getBarPositions();

        int barToFace = 0;

        if (this.rotation != 2) {
            if (petEntity.getPosition().getX() != this.getPosition().getX())
                barToFace = 1;
        } else {
            if (petEntity.getPosition().getY() != this.getPosition().getY())
                barToFace = 1;
        }

        final Position bar = this.getBarPositions()[barToFace];

        if (!entity.hasStatus(RoomEntityStatus.JUMP) && this.petEntity != entity) {
            entity.lookTo(bar.getX(), bar.getY());

            entity.getMountedEntity().moveTo(entity.getPosition().squareInFront(entity.getBodyRotation(), 4));
            entity.addStatus(RoomEntityStatus.JUMP, "1.1");
            entity.markNeedsUpdate();

            this.petEntity = petEntity;
        }

        if (positions[0].equals(entity.getPosition()) || positions[1].equals(entity.getPosition())) {
            if (petEntity.getData().getTypeId() == PetType.HORSE) {
                entity.removeStatus(RoomEntityStatus.JUMP);
                entity.markNeedsUpdate();

                this.setTicks(RoomItemFactory.getProcessTime(0.5));
            }
        }
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if (entity instanceof PetEntity && ((PetEntity) entity).getData().getTypeId() == PetType.HORSE) {
            entity.removeStatus(RoomEntityStatus.JUMP);

            this.petEntity = null;
        }
    }

    @Override
    public void onUnload() {
        this.petEntity = null;
    }

    @Override
    public boolean isMovementCancelled(RoomEntity entity, Position position) {
        final Position[] barPos = this.getBarPositions();
        final boolean barPosEq = (barPos[0].getX() == position.getX() && barPos[0].getY() == position.getY()) ||
                (barPos[1].getX() == position.getX() && barPos[1].getY() == position.getY());

        if (entity.getMountedEntity() == null && barPosEq) {
            return true;
        }

        return false;
    }

    private Position[] getBarPositions() {
        Position a = this.getPosition().copy();
        Position b = this.getPosition().copy();

        if (this.rotation == 2) {
            a.setX(a.getX() + 1);
        } else {
            a.setY(a.getY() + 1);
        }

        b.setX(b.getX() + 1);
        b.setY(b.getY() + 1);

        return new Position[]{
                a, b
        };
    }
}
