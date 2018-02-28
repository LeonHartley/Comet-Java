package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.types.floor.pet.breeding.BreedingBoxFloorItem;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class BreedPetsMessageEvent implements com.cometproject.server.network.messages.incoming.Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int state = msg.readInt();
        final String name = msg.readString();
        final int fatherId = msg.readInt();
        final int motherId = msg.readInt();

        if (client.getPlayer().getEntity() == null) {
            return;
        }

        final PetEntity father = client.getPlayer().getEntity().getRoom().getEntities().getEntityByPetId(fatherId);
        final PetEntity mother = client.getPlayer().getEntity().getRoom().getEntities().getEntityByPetId(motherId);

        if (father == null || mother == null) {
            // pets cant be found.
            return;
        }

        // check positions
        if (father.getPosition().getX() != mother.getPosition().getX() || father.getPosition().getY() != mother.getPosition().getY()) {
            return;
        }

        // make sure the pets are of the same type
        if (father.getData().getTypeId() != mother.getData().getTypeId()) {
            return;
        }

        if (!(father.getTile().getTopItemInstance() instanceof BreedingBoxFloorItem)) {
            return;
        }

        final BreedingBoxFloorItem breedingBoxFloorItem = (BreedingBoxFloorItem) father.getTile().getTopItemInstance();
        breedingBoxFloorItem.begin(client.getPlayer(), name);
    }
}
