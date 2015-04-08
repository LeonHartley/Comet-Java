package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarsMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class PlacePetMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
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

            Position position = new Position(x, y, room.getMapping().getTile(x, y).getWalkHeight());
//            Position position = new Position(x, y, room.getModel().getSquareHeight()[x][y]);


            if ((!atDoor && client.getPlayer().getEntity().getRoom().getEntities().getEntitiesAt(position).size() >= 1) || !room.getMapping().isValidPosition(position)) {
                return;
            }

            PetEntity petEntity = client.getPlayer().getEntity().getRoom().getPets().addPet(pet, position);

            client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new AvatarsMessageComposer(petEntity));

            for(RoomItemFloor floorItem : room.getItems().getItemsOnSquare(x, y)) {
                floorItem.onEntityStepOn(petEntity);
            }

            client.getPlayer().getPets().removePet(pet.getId());
            client.send(new PetInventoryMessageComposer(client.getPlayer().getPets().getPets()));
        }
    }
}
