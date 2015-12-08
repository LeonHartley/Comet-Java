package com.cometproject.server.network.messages.incoming.user.camera;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.RoomNotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.google.common.collect.Lists;

public class TakePhotoMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final String code = msg.readString();
        final String itemExtraData = "{\"t\":" + System.currentTimeMillis() + ",\"u\":\"" + code + "\",\"n\":\"" + client.getPlayer().getData().getUsername() + "\",\"m\":\"\",\"s\":" + client.getPlayer().getId() + ",\"w\":\"" + CometSettings.cameraPhotoUrl.replace("%photoId%", code) + "\"}";

        int itemId = ItemDao.createItem(client.getPlayer().getId(), CometSettings.cameraPhotoItemId, itemExtraData);
        final InventoryItem inventoryItem = new InventoryItem(itemId, CometSettings.cameraPhotoItemId, itemExtraData);

        client.getPlayer().getInventory().addItem(inventoryItem);

        client.send(new RoomNotificationMessageComposer("generic", Locale.getOrDefault("camera.photoTaken", "You successfully took a photo!")));
        client.send(new UpdateInventoryMessageComposer());

        client.send(new UnseenItemsMessageComposer(Lists.newArrayList(inventoryItem)));

        client.getPlayer().getAchievements().progressAchievement(AchievementType.CAMERA_PHOTO, 1);
    }
}
