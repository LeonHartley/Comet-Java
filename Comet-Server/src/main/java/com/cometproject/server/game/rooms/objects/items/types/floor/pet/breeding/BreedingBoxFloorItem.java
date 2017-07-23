package com.cometproject.server.game.rooms.objects.items.types.floor.pet.breeding;

import com.cometproject.server.game.pets.PetManager;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.pets.races.PetBreedLevel;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.entities.types.ai.pets.PetAI;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.pets.breeding.PetBreedingCompleteMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.pets.breeding.PetBreedingMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.pets.breeding.PetBreedingStartedMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.pets.PetDao;
import com.cometproject.server.utilities.RandomUtil;

import java.util.Set;

public abstract class BreedingBoxFloorItem extends DefaultFloorItem {

    private PetEntity mother;
    private PetEntity father;
    private Player breeder;
    private String petName;

    public BreedingBoxFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    public void begin(Player breeder, final String name) {
        // perform necessary checks.
        if(this.mother == null || this.father == null) {
            // error!
            return;
        }

        this.petName = name;
        this.breeder = breeder;

        breeder.getSession().send(new PetBreedingStartedMessageComposer(this.getVirtualId(), 0));

        this.setExtraData("1");
        this.sendUpdate();

        this.setTicks(RoomItemFactory.getProcessTime(2.5));
    }

    @Override
    public void onTickComplete() {
        final int randomInt = RandomUtil.getRandomInt(1, 100);
        PetBreedLevel rarityLevel = PetBreedLevel.COMMON;

        if(randomInt == 1) {
            rarityLevel = PetBreedLevel.EPIC;
        } else if(randomInt <= 2) {
            rarityLevel = PetBreedLevel.RARE;
        } else if(randomInt <= 5) {
            rarityLevel = PetBreedLevel.UNCOMMON;
        }

        final Set<Integer> availableBreeds = PetManager.getInstance().getPetBreedPallets().get(this.getBabyType()).get(rarityLevel);
        int breed = WiredUtil.getRandomElement(availableBreeds);

        // create pet with result breed
        final int petId = PetDao.createPet(this.ownerId, petName, this.getBabyType(), breed, "FFFFFF");
        final PetData petData = new PetData(petId, this.petName, this.ownerId, this.ownerName, breed, this.getBabyType());

        PetEntity petEntity = this.getRoom().getPets().addPet(petData, this.getPosition());

        this.getRoom().getEntities().broadcastMessage(new AvatarsMessageComposer(petEntity));

        this.breeder.getSession().send(new PetBreedingCompleteMessageComposer(petData.getId(), rarityLevel.getLevelId()));

        this.getTile().getEntities().add(petEntity);

        ((PetAI) this.mother.getAI()).breedComplete();
        ((PetAI) this.father.getAI()).breedComplete();

        this.setExtraData("2");
        this.sendUpdate();

        this.getRoom().getItems().removeItem(this, null, false, true);
    }

    @Override
    public boolean isMovementCancelled(RoomEntity entity) {
        if (!(entity instanceof PetEntity)) {
            return true;
        }

        final PetEntity petEntity = (PetEntity) entity;

        if (petEntity.getData() != null) {
            if (petEntity.getData().getTypeId() == this.getPetType()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onEntityStepOn(final RoomEntity entity) {
        // begin breeding if there's 2 of the same type of pet.
        if(this.mother == null && entity instanceof PetEntity) {
            if(((PetEntity) entity).getData().getTypeId() == this.getPetType()) {
                this.mother = ((PetEntity) entity);
            }
        } else if(this.father == null && entity instanceof PetEntity) {
            if(((PetEntity) entity).getData().getTypeId() == this.getPetType()) {
                this.father = ((PetEntity) entity);
            }
        }

        if(this.getTile().getEntities().size() == 2) {
            final PlayerEntity playerEntity = this.getRoom().getEntities().getEntityByPlayerId(this.getOwner());
            playerEntity.getPlayer().getSession().send(new PetBreedingMessageComposer(this.getVirtualId(), this.getBabyType(), this.mother.getData(), this.father.getData()));
        }
    }

    @Override
    public void onEntityStepOff(final RoomEntity entity) {
        if(entity == this.mother) {
            this.mother = null;
        } else if(entity == this.father) {
            this.father = null;
        }
    }

    protected abstract int getBabyType();

    protected abstract int getPetType();
}
