package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.entities.types.PetEntity;
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

        if (client.getPlayer().getId() != entity.getData().getOwnerId() || !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        client.getPlayer().getPets().addPet(entity.getData());
        client.send(PetInventoryMessageComposer.compose(client.getPlayer().getPets().getPets()));

        entity.leaveRoom(false, false, false);

        RoomPetDao.updatePet(0, 0, 0, entity.getData().getId());
    }
}
