package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.misc.Position;
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

        boolean atDoor = false;

        if (x == 0 && y == 0) {
            x = client.getPlayer().getEntity().getRoom().getModel().getDoorX();
            y = client.getPlayer().getEntity().getRoom().getModel().getDoorY();

            atDoor = true;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        PetData pet = client.getPlayer().getPets().getPet(petId);

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();

        if ((isOwner || client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            if (pet == null) {
                return;
            }

            if ((!atDoor && client.getPlayer().getEntity().getRoom().getEntities().getEntitiesAt(x, y).size() >= 1) || !room.getMapping().isValidPosition(new Position(x, y, room.getModel().getSquareHeight()[x][y]))) {
                return;
            }

            PetEntity petEntity = client.getPlayer().getEntity().getRoom().getPets().addPet(pet, x, y);

            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(AvatarsMessageComposer.compose(petEntity));

            client.getPlayer().getPets().removePet(pet.getId());
            client.send(PetInventoryMessageComposer.compose(client.getPlayer().getPets().getPets()));
        }
    }
}
