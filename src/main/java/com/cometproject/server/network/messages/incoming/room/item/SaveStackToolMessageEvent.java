package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.items.types.floor.MagicStackFloorItem;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class SaveStackToolMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int itemId = msg.readInt();
        int height = msg.readInt();

        if (height < 0 && height != -100) {
            return;
        }

        System.out.println(height);

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) return;

        RoomItemFloor floorItem = client.getPlayer().getEntity().getRoom().getItems().getFloorItem(itemId);

        if (floorItem == null || !(floorItem instanceof MagicStackFloorItem)) return;

        MagicStackFloorItem magicStackFloorItem = ((MagicStackFloorItem) floorItem);

        if (height == -100) {
            magicStackFloorItem.setMagicHeight(
                    magicStackFloorItem.getRoom().getMapping().getTile(
                            magicStackFloorItem.getX(),
                            magicStackFloorItem.getY()
                    ).getOriginalHeight());
        } else {
            double heightf = height / 100.0d;

            System.out.println(heightf);

            magicStackFloorItem.setMagicHeight(heightf);
        }

        for (AffectedTile affectedTile : AffectedTile.getAffectedBothTilesAt(
                magicStackFloorItem.getDefinition().getLength(),
                magicStackFloorItem.getDefinition().getWidth(),
                magicStackFloorItem.getX(),
                magicStackFloorItem.getY(),
                magicStackFloorItem.getRotation())) {

            magicStackFloorItem.getRoom().getMapping().getTile(affectedTile.x, affectedTile.y).reload();
        }

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(UpdateFloorItemMessageComposer.compose(magicStackFloorItem));
        magicStackFloorItem.saveData();
    }
}
