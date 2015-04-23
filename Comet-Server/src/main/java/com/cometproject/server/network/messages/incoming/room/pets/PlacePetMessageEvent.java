package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.game.rooms.types.mapping.Tile;
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

        RoomInstance room = client.getPlayer().getEntity().getRoom();

        PetData pet = client.getPlayer().getPets().getPet(petId);

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();

        if ((isOwner || client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            if (pet == null) {
                return;
            }

            Tile tile = room.getMapping().getTile(x, y);

            if(tile == null) return;

            Position position = new Position(x, y, tile.getWalkHeight());

            if ((!atDoor && tile.getEntities().size() >= 1) || !room.getMapping().isValidPosition(position)) {
                return;
            }

            PetEntity petEntity = room.getPets().addPet(pet, position);

            room.getEntities().broadcastMessage(new AvatarsMessageComposer(petEntity));

            for(RoomItemFloor floorItem : room.getItems().getItemsOnSquare(x, y)) {
                floorItem.onEntityStepOn(petEntity);
            }

            tile.getEntities().add(petEntity);

            client.getPlayer().getPets().removePet(pet.getId());
            client.send(new PetInventoryMessageComposer(client.getPlayer().getPets().getPets()));
        }
    }
}
