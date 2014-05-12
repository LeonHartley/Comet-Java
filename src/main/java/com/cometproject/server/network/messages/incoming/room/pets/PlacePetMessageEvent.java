package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class PlacePetMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int petId = msg.readInt();
        int x = msg.readInt();
        int y = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();

        PetData pet = client.getPlayer().getPets().getPet(petId);

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();

        if ((isOwner || client.getPlayer().getPermissions().hasPermission("room_full_control") || room.getData().isAllowPets())) {
            if (pet == null) {
                return;
            }

            if (client.getPlayer().getEntity().getRoom().getEntities().getEntitiesAt(x, y).size() >= 1 || !room.getMapping().isValidPosition(new Position3D(x, y, room.getModel().getSquareHeight()[x][y]))) {
                return;
            }

            PetEntity petEntity = client.getPlayer().getEntity().getRoom().getPets().addPet(pet, x, y);

            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(AvatarsMessageComposer.compose(petEntity));

            client.getPlayer().getPets().removePet(pet.getId());
            client.send(PetInventoryMessageComposer.compose(client.getPlayer().getPets().getPets()));
        }
    }
}
