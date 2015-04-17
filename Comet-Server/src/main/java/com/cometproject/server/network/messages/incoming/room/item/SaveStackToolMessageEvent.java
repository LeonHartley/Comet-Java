package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.objects.entities.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.MagicStackFloorItem;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.game.rooms.types.mapping.Tile;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.engine.UpdateStackMapMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class SaveStackToolMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        RoomInstance room = client.getPlayer().getEntity().getRoom();

        if (room == null) {
            return;
        }
        if (!room.getRights().hasRights(client.getPlayer().getEntity().getPlayerId()) && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            client.disconnect();
            return;
        }

        int itemId = msg.readInt();
        int height = msg.readInt();

        if (height < 0 && height != -100) {
            return;
        }

        RoomItemFloor floorItem = room.getItems().getFloorItem(itemId);

        if (floorItem == null || !(floorItem instanceof MagicStackFloorItem)) return;

        MagicStackFloorItem magicStackFloorItem = ((MagicStackFloorItem) floorItem);

        if (height == -100) {
            magicStackFloorItem.setOverrideHeight(
                    magicStackFloorItem.getRoom().getMapping().getTile(
                            magicStackFloorItem.getPosition().getX(),
                            magicStackFloorItem.getPosition().getY()
                    ).getOriginalHeight());
        } else {
            double heightf = height / 100.0d;

            magicStackFloorItem.setOverrideHeight(heightf);
        }

        for (AffectedTile affectedTile : AffectedTile.getAffectedBothTilesAt(
                magicStackFloorItem.getDefinition().getLength(),
                magicStackFloorItem.getDefinition().getWidth(),
                magicStackFloorItem.getPosition().getX(),
                magicStackFloorItem.getPosition().getY(),
                magicStackFloorItem.getRotation())) {

            Tile tile = magicStackFloorItem.getRoom().getMapping().getTile(affectedTile.x, affectedTile.y);

            if(tile != null) {
                tile.reload();

                client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new UpdateStackMapMessageComposer(tile));
            }
        }

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new UpdateFloorItemMessageComposer(magicStackFloorItem));
        magicStackFloorItem.saveData();
    }
}
