package com.cometproject.server.network.messages.incoming.user.wardrobe;

import com.cometproject.api.game.catalog.types.IClothingItem;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.wardrobe.FigureSetIdsMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.player.PlayerClothingDao;

public class RedeemClothingMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int itemId = msg.readInt();

        if (client.getPlayer() == null || client.getPlayer().getEntity() == null) {
            return;
        }

        final PlayerEntity playerEntity = client.getPlayer().getEntity();

        if (playerEntity.getRoom() == null) {
            return;
        }

        final Room room = playerEntity.getRoom();
        final RoomItemFloor floorItem = room.getItems().getFloorItem(itemId);

        if (floorItem == null || floorItem.getItemData().getOwnerId() != playerEntity.getPlayerId()) {
            return;
        }

        final IClothingItem clothingItem =
                CatalogManager.getInstance().getClothingItems().get(floorItem.getDefinition().getItemName());

        if (clothingItem == null) {
            return;
        }

        client.getPlayer().getWardrobe().getClothing().add(clothingItem.getItemName());
        PlayerClothingDao.redeemClothing(client.getPlayer().getId(), clothingItem.getItemName());

        room.getItems().removeItem(floorItem, client, false, true);
        client.send(new FigureSetIdsMessageComposer(client.getPlayer().getWardrobe().getClothing()));
        client.send(new NotificationMessageComposer("figureset.redeemed.success"));
    }
}
