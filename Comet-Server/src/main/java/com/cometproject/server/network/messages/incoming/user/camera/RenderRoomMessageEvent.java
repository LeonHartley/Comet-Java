package com.cometproject.server.network.messages.incoming.user.camera;

import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.server.api.ApiClient;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.camera.PhotoPreviewMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.UnseenItemsMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.items.ItemDao;
import com.google.common.collect.Sets;

import java.util.UUID;

public class RenderRoomMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int length = msg.readInt();
        final byte[] payload = msg.readBytes(length);

        // Save the image
        final UUID photoId = UUID.randomUUID();
        final String response = ApiClient.getInstance().savePhoto(payload, photoId.toString());

        if (response.isEmpty()) {
            // Failed, send feedback to client
            return;
        }

        client.getPlayer().setLastPhoto(photoId.toString());
        client.send(new PhotoPreviewMessageComposer(photoId.toString()));
    }
}
