package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.eggs.PetPackageFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.pets.PetPackageCompleteMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.pets.PetDao;

public class OpenPetPackageMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int itemId = msg.readInt();
        final String petName = msg.readString();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        final Room room = client.getPlayer().getEntity().getRoom();
        final RoomItemFloor floor = room.getItems().getFloorItem(itemId);

        if (floor == null || floor.getItemData().getOwnerId() != client.getPlayer().getId()) {
            return;
        }

        final PetPackageFloorItem petPackage = (PetPackageFloorItem) floor;

        // todo: validate name
        final int raceId = petPackage.getRaceId();
        final int petId = PetDao.createPet(floor.getItemData().getOwnerId(), petName, petPackage.getPetTypeId(), raceId, "FFFFFF");
        final PetData petData = new PetData(petId, petName, floor.getItemData().getOwnerId(), floor.getItemData().getOwnerName(), raceId, ((PetPackageFloorItem) floor).getPetTypeId());

        PetEntity petEntity = room.getPets().addPet(petData, floor.getPosition());
        petEntity.getPetAI().free();

        room.getItems().removeItem(floor, null, false, true);

        client.send(new PetPackageCompleteMessageComposer(itemId, 0, petName));
        room.getEntities().broadcastMessage(new AvatarsMessageComposer(petEntity));
    }
}
