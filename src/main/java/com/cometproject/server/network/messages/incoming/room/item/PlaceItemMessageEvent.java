package com.cometproject.server.network.messages.incoming.room.item;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.components.types.InventoryItem;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.items.SendFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.SendWallItemMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastList;

import java.sql.PreparedStatement;
import java.util.List;

public class PlaceItemMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String[] parts = msg.readString().split(" ");
        int id = Integer.parseInt(parts[0].replace("-", ""));

        if(!client.getPlayer().getData().getUsername().equals(client.getPlayer().getEntity().getRoom().getData().getOwner())) {
            return;
        }

        try {
            if(parts[1].startsWith(":")) {
                String position = Position3D.validateWallPosition(parts[1] + " " + parts[2] + " " + parts[3]);

                if(position == null) {
                    return;
                }

                InventoryItem item = client.getPlayer().getInventory().getWallItem(id);
                PreparedStatement query = Comet.getServer().getStorage().prepare("UPDATE items SET room_id = ?, wall_pos = ?, extra_data = ? WHERE id = ?;");

                query.setInt(1, client.getPlayer().getEntity().getRoom().getId());
                query.setString(2, position);
                query.setString(3, (item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) ? "0" : item.getExtraData());
                query.setInt(4, item.getId());

                query.executeUpdate();

                client.getPlayer().getInventory().removeWallItem(id);

                Room r = client.getPlayer().getEntity().getRoom();

                client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(
                        SendWallItemMessageComposer.compose(
                                r.getItems().addWallItem(id, item.getBaseId(), client.getPlayer().getId(), r.getId(), position, (item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) ? "0" : item.getExtraData()),
                                r
                        )
                );
            } else {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int rot = Integer.parseInt(parts[3]);

                InventoryItem item = client.getPlayer().getInventory().getFloorItem(id);
                float height = (float) client.getPlayer().getEntity().getRoom().getModel().getSquareHeight()[x][y];

                for(FloorItem stackItem : client.getPlayer().getEntity().getRoom().getItems().getItemsOnSquare(x, y)) {
                    if(item.getId() != stackItem.getId()) {
                        if(stackItem.getDefinition().canStack) {
                            height+= stackItem.getDefinition().getHeight();
                        } else {
                            return;
                        }
                    }
                }

                PreparedStatement query = Comet.getServer().getStorage().prepare("UPDATE items SET room_id = ?, x = ?, y = ?, z = ?, rot = ?, extra_data = ? WHERE id = ?;");

                query.setInt(1, client.getPlayer().getEntity().getRoom().getId());
                query.setInt(2, x);
                query.setInt(3, y);
                query.setFloat(4, height);
                query.setInt(5, rot);
                query.setString(6, (item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) ? "0" : item.getExtraData());
                query.setInt(7, id);

                query.executeUpdate();

                client.getPlayer().getInventory().removeFloorItem(id);

                Room room = client.getPlayer().getEntity().getRoom();

                FloorItem floorItem = room.getItems().addFloorItem(id, item.getBaseId(), room.getId(), client.getPlayer().getId(), x, y, rot, height, (item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) ? "0" : item.getExtraData());
                List<Position3D> tilesToUpdate = new FastList<>();

                tilesToUpdate.add(new Position3D(floorItem.getX(), floorItem.getY(), 0d));

                for (AffectedTile tile : AffectedTile.getAffectedTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(), floorItem.getX(), floorItem.getY(), floorItem.getRotation())) {
                    tilesToUpdate.add(new Position3D(tile.x, tile.y, 0d));
                }

                for(Position3D tileToUpdate : tilesToUpdate) {
                    room.getMapping().updateTile(tileToUpdate.getX(), tileToUpdate.getY());
                }

                room.getEntities().broadcastMessage(SendFloorItemMessageComposer.compose(floorItem, room));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
