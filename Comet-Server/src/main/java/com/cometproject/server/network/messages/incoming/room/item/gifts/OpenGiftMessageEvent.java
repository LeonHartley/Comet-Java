package com.cometproject.server.network.messages.incoming.room.item.gifts;

import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.GiftFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.gifts.OpenGiftMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;


public class OpenGiftMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final long floorItemId = ItemManager.getInstance().getItemIdByVirtualId(msg.readInt());

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) return;

        Room room = client.getPlayer().getEntity().getRoom();
        RoomItemFloor floorItem = room.getItems().getFloorItem(floorItemId);

        if (floorItem == null || !(floorItem instanceof GiftFloorItem)) return;

        if (floorItem.getOwner() != client.getPlayer().getId() && !client.getPlayer().getPermissions().getRank().roomFullControl()) {
            return;
        }

        final GiftData giftData = ((GiftFloorItem) floorItem).getGiftData();

        final CatalogPage catalogPage = CatalogManager.getInstance().getPage(giftData.getPageId());
        if (catalogPage == null) return;

        final CatalogItem catalogItem = catalogPage.getItems().get(giftData.getItemId());
        if (catalogItem == null) return;

        floorItem.onInteract(client.getPlayer().getEntity(), 0, false);

        room.getItems().removeItem(floorItem, client);

        client.getPlayer().getEntity().getRoom().getItems().placeFloorItem(new InventoryItem(floorItemId, Integer.parseInt(catalogItem.getItemId()), giftData.getExtraData()), floorItem.getPosition().getX(), floorItem.getPosition().getY(), floorItem.getRotation(), client.getPlayer());
        client.send(new OpenGiftMessageComposer(ItemManager.getInstance().getItemVirtualId(floorItemId), floorItem.getDefinition().getType(), ((GiftFloorItem) floorItem).getGiftData(), ItemManager.getInstance().getDefinition(catalogItem.getItems().get(0).getItemId())));

        RoomItemDao.setBaseItem(floorItemId, catalogItem.getItems().get(0).getItemId());
    }
}
