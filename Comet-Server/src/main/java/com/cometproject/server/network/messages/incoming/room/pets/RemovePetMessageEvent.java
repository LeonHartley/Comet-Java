package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.api.game.pets.IPetData;
import com.cometproject.api.game.rooms.settings.RoomKickState;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.pets.RoomPetDao;


public class RemovePetMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (client.getPlayer() == null || client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null)
            return;

        int petId = msg.readInt();

        PetEntity entity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByPetId(petId);

        if (entity == null) {
            final PlayerEntity playerEntity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByPlayerId(petId);

            if (playerEntity == null) {
                return;
            }

            if (playerEntity.hasAttribute("transformation")) {
                if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId())
                        && !client.getPlayer().getPermissions().getRank().roomFullControl() && client.getPlayer().getEntity().getRoom().getData().getKickState() != RoomKickState.EVERYONE) {
                    return;
                }

                if (client.getPlayer().getEntity().getRoom().getData().getOwnerId() == playerEntity.getPlayerId() || !playerEntity.getPlayer().getPermissions().getRank().roomKickable()) {
                    return;
                }

                playerEntity.kick();
            }

            return;
        }

        Room room = entity.getRoom();

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();

        if ((isOwner) || client.getPlayer().getPermissions().getRank().roomFullControl() || (room.getData().isAllowPets() && entity.getData().getOwnerId() == client.getPlayer().getId())) {
            int ownerId = entity.getData().getOwnerId();

            if (room.getData().isAllowPets() || client.getPlayer().getId() != ownerId) {
                if (NetworkManager.getInstance().getSessions().getByPlayerId(ownerId) != null) {
                    Session petOwner = NetworkManager.getInstance().getSessions().getByPlayerId(ownerId);

                    givePetToPlayer(petOwner, entity.getData());
                }
            } else {
                givePetToPlayer(client, entity.getData());
            }

            RoomPetDao.updatePet(0, 0, 0, entity.getData().getId());
            entity.leaveRoom(false);
        }
    }

    private void givePetToPlayer(ISession client, IPetData petData) {
        client.getPlayer().getPets().addPet(petData);
        client.send(new PetInventoryMessageComposer(client.getPlayer().getPets().getPets()));
    }
}
