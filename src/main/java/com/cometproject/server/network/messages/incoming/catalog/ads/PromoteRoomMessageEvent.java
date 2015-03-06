package com.cometproject.server.network.messages.incoming.catalog.ads;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.BoughtItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class PromoteRoomMessageEvent implements IEvent {

    @Override
    public void handle(Session client, Event msg) throws Exception {
        int pageId = msg.readInt();
        int itemId = msg.readInt();

        CatalogPage page = CatalogManager.getInstance().getPage(pageId);

        if (page == null || page.getItems().get(itemId) == null) return;

        CatalogItem item = page.getItems().get(itemId);

        if (item == null) return;

        RoomData roomData = RoomManager.getInstance().getRoomData(msg.readInt());

        if (roomData == null || roomData.getOwnerId() != client.getPlayer().getId() || !roomData.getAccess().equals("open")) {
            return;
        }

        final int totalCostCredits = item.getCostCredits();
        final int totalCostPoints = item.getCostOther();
        final int totalCostActivityPoints = item.getCostActivityPoints();

        if (client.getPlayer().getData().getCredits() < totalCostCredits || client.getPlayer().getData().getVipPoints() < totalCostPoints || client.getPlayer().getData().getActivityPoints() < totalCostActivityPoints) {
            CometManager.getLogger().warn("Player with ID: " + client.getPlayer().getId() + " tried to purchase item with ID: " + item.getId() + " with the incorrect amount of credits, points or activity points.");
            client.send(new AlertMessageComposer(Locale.get("catalog.error.notenough")));
            return;
        }

        client.getPlayer().getData().decreaseCredits(totalCostCredits);
        client.getPlayer().getData().decreasePoints(totalCostPoints);
        client.getPlayer().getData().decreaseActivityPoints(totalCostActivityPoints);

        client.getPlayer().sendBalance();
        client.getPlayer().getData().save();

        String promotionName = msg.readString();
        boolean bool = msg.readBoolean();
        String promotionDescription = msg.readString();

        if (promotionName == null || promotionDescription == null || promotionName.isEmpty() || promotionDescription.isEmpty()) {
            return;
        }

        if(item.getBadgeId() != null && !item.getBadgeId().isEmpty()) {
            client.getPlayer().getInventory().addBadge(item.getBadgeId(), true);
        }

        RoomManager.getInstance().promoteRoom(roomData.getId(), promotionName, promotionDescription);
        client.send(new BoughtItemMessageComposer(BoughtItemMessageComposer.PurchaseType.BADGE));
    }
}
