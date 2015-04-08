package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.PapersMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import org.apache.log4j.Logger;

import java.util.Map;


public class UpdatePapersMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) {
        int itemId = msg.readInt();

        InventoryItem item = client.getPlayer().getInventory().getWallItem(itemId);

        if (item == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();
        boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());

        if (isOwner || hasRights) {
            String type = "floor";
            Map<String, String> decorations = room.getData().getDecorations();
            String data = item.getExtraData();

            if (item.getDefinition().getItemName().contains("wallpaper")) {
                type = "wallpaper";
            } else if (item.getDefinition().getItemName().contains("landscape")) {
                type = "landscape";
            }

            if (decorations.containsKey(type)) {
                decorations.replace(type, data);
            } else {
                decorations.put(type, data);
            }

            client.getPlayer().getInventory().removeItem(item);
            RoomItemDao.deleteItem(itemId);
            client.send(new UpdateInventoryMessageComposer());

            try {
                room.getData().save();
                room.getEntities().broadcastMessage(new PapersMessageComposer(type, data));
            } catch (Exception e) {
                Logger.getLogger(UpdatePapersMessageEvent.class.getName()).error("Error while saving room data", e);
            }
        }
    }
}
