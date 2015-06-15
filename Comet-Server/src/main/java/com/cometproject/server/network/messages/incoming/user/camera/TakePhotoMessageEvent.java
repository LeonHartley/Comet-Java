package com.cometproject.server.network.messages.incoming.user.camera;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.items.ItemDao;

public class TakePhotoMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final String code = msg.readString();
        final String itemExtraData = "{\"t\":1,\"u\":\"1\",\"m\":\"\",\"s\":1,\"w\":\"" + CometSettings.cameraPhotoUrl.replace("%photoId%", code) + "\"}";

        int itemId = ItemDao.createItem(client.getPlayer().getId(), CometSettings.cameraPhotoItemId, itemExtraData);
        client.getPlayer().getInventory().addItem(new InventoryItem(itemId, CometSettings.cameraPhotoItemId, itemExtraData));

        client.send(new MotdNotificationComposer("You took a photo!"));
        client.send(new UpdateInventoryMessageComposer());
    }
}
