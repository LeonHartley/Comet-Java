package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.components.types.InventoryItem;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.WallItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.SendFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SendWallItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

import java.util.ArrayList;
import java.util.List;

public class  PlaceItemMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String[] parts = msg.readString().split(" ");
        int id = Integer.parseInt(parts[0].replace("-", ""));

        if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId()) && !client.getPlayer().getPermissions().hasPermission("room_full_control")) {
            return;
        }

        try {
            if (parts[1].startsWith(":")) {
                String position = Position3D.validateWallPosition(parts[1] + " " + parts[2] + " " + parts[3]);

                if (position == null) {
                    return;
                }

                InventoryItem item = client.getPlayer().getInventory().getWallItem(id);

                RoomItemDao.placeWallItem(client.getPlayer().getEntity().getRoom().getId(), position, (item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) ? "0" : item.getExtraData(), item.getId());
                client.getPlayer().getInventory().removeWallItem(id);

                Room room = client.getPlayer().getEntity().getRoom();
                WallItem wallItem = room.getItems().addWallItem(id, item.getBaseId(), client.getPlayer().getId(), room.getId(), position, (item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) ? "0" : item.getExtraData());

                client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(
                        SendWallItemMessageComposer.compose(wallItem, room)
                );

            } else {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int rot = Integer.parseInt(parts[3]);

                InventoryItem item = client.getPlayer().getInventory().getFloorItem(id);

                if(item == null)
                    return;

                /*TileInstance tile = client.getPlayer().getEntity().getRoom().getMapping().getTile(x, y);

                if(!tile.canStack())
                    return;

                float height = (float) tile.getStackHeight();*/

                Room room = client.getPlayer().getEntity().getRoom();

                float height = (float) client.getPlayer().getEntity().getRoom().getModel().getSquareHeight()[x][y];

                for (FloorItem stackItem : room.getItems().getItemsOnSquare(x, y)) {
                    if (item.getId() != stackItem.getId()) {
                        if (stackItem.getDefinition().canStack) {
                            height += stackItem.getDefinition().getHeight();
                        } else {
                            return;
                        }
                    }
                }

                if (item.getDefinition() != null && item.getDefinition().getInteraction() != null) {
                    if (item.getDefinition().getInteraction().equals("mannequin")) {
                        rot = 2;
                    }
                }

                RoomItemDao.placeFloorItem(room.getId(), x, y, height, rot, (item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) ? "0" : item.getExtraData(), id);
                client.getPlayer().getInventory().removeFloorItem(id);

                FloorItem floorItem = room.getItems().addFloorItem(id, item.getBaseId(), room.getId(), client.getPlayer().getId(), x, y, rot, height, (item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) ? "0" : item.getExtraData(), item.getGiftData());
                List<Position3D> tilesToUpdate = new ArrayList<>();

                tilesToUpdate.add(new Position3D(floorItem.getX(), floorItem.getY(), 0d));

                for (AffectedTile affTile : AffectedTile.getAffectedTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(), floorItem.getX(), floorItem.getY(), floorItem.getRotation())) {
                    tilesToUpdate.add(new Position3D(affTile.x, affTile.y, 0d));
                }

                for (Position3D tileToUpdate : tilesToUpdate) {
                    room.getMapping().updateTile(tileToUpdate.getX(), tileToUpdate.getY());
                }

                room.getEntities().broadcastMessage(SendFloorItemMessageComposer.compose(floorItem, room));
            }
        } catch (Exception e) {
            CometManager.getLogger().error("Error while placing item", e);
        }
    }
}
