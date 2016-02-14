package com.cometproject.server.network.messages.incoming.room.pets.horse;

import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.pets.horse.HorseFigureMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class ApplyHorseEffectMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int itemId = msg.readInt();
        final int petId = msg.readInt();

        if(client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        final Room room = client.getPlayer().getEntity().getRoom();
        RoomItemFloor saddleItem = room.getItems().getFloorItem(ItemManager.getInstance().getItemIdByVirtualId(itemId));

        if(saddleItem == null) {
            return;
        }

        PetEntity petEntity = room.getEntities().getEntityByPetId(petId);

        if(petEntity == null || petEntity.getData().getOwnerId() != client.getPlayer().getId()) {
            return;
        }

        petEntity.getData().setSaddled(true);
        petEntity.getData().saveHorseData();

        room.getEntities().broadcastMessage(new HorseFigureMessageComposer(petEntity));

        room.getItems().removeItem(saddleItem, client, false, true);
    }
}
