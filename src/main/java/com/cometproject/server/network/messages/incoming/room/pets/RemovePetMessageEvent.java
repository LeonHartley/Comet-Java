package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.pets.RoomPetDao;

public class RemovePetMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        PetEntity entity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByPetId(msg.readInt());

        if (entity == null) {
            return;
        }

        Room room = entity.getRoom();

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();

        if ((isOwner) || client.getPlayer().getPermissions().hasPermission("room_full_control") || (room.getData().isAllowPets() && entity.getData().getOwnerId() == client.getPlayer().getId())) {
            int ownerId = entity.getData().getOwnerId();

            if (room.getData().isAllowPets() || client.getPlayer().getId() != ownerId) {
                if (Comet.getServer().getNetwork().getSessions().getByPlayerId(ownerId) != null) {
                    Session petOwner = Comet.getServer().getNetwork().getSessions().getByPlayerId(ownerId);

                    givePetToPlayer(petOwner, entity.getData());
                }
            } else {
                givePetToPlayer(client, entity.getData());
            }

            RoomPetDao.updatePet(0, 0, 0, entity.getData().getId());
            entity.leaveRoom(false);
        }
    }

    private void givePetToPlayer(Session client, PetData petData) {
        client.getPlayer().getPets().addPet(petData);
        client.send(PetInventoryMessageComposer.compose(client.getPlayer().getPets().getPets()));
    }
}
