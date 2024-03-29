package com.cometproject.server.network.messages.incoming.user.camera;

import com.cometproject.server.api.ApiClient;
import com.cometproject.server.composers.camera.PhotoPreviewMessageComposer;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

import java.util.UUID;

public class RenderRoomMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        final int length = msg.readInt();
        final byte[] payload = msg.readBytes(length);

        final String id = UUID.randomUUID().toString();
        final String response = ApiClient.getInstance().savePhoto(payload, id);

        if (response.isEmpty()) {
            // Failed, send feedback to client
            client.getPlayer().sendNotif("Camera", "Failed to upload image");
            return;
        }

        client.getPlayer().setLastPhoto(id);
        client.send(new PhotoPreviewMessageComposer(id));
    }
}
