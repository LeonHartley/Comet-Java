package com.cometproject.server.network.messages.incoming.room.settings;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.components.types.InventoryItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.engine.PapersMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.InventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import org.apache.log4j.Logger;

import java.util.Map;

public class UpdatePapersMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        int itemId = msg.readInt();

        InventoryItem item = client.getPlayer().getInventory().getWallItem(itemId);

        if(item == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();
        boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());

        if(isOwner || hasRights) {
            String type = "floor";
            Map<String, String> decorations = room.getData().getDecorations();
            String data = item.getExtraData();

            if(item.getDefinition().getItemName().contains("wallpaper")) {
                type = "wallpaper";
            } else if(item.getDefinition().getItemName().contains("landscape")) {
                type = "landscape";
            }

            if(decorations.containsKey(type)) {
                decorations.replace(type, data);
            } else {
                decorations.put(type, data);
            }

            client.getPlayer().getInventory().removeItem(item);
            Comet.getServer().getStorage().execute("DELETE FROM items WHERE id = " + item.getId());
            client.send(UpdateInventoryMessageComposer.compose());

            try {
                room.getData().save();
                room.getEntities().broadcastMessage(PapersMessageComposer.compose(type, data));
            } catch(Exception e) {
                Logger.getLogger(UpdatePapersMessageEvent.class.getName()).error("Error while saving room data", e);
            }
        }
    }
}
