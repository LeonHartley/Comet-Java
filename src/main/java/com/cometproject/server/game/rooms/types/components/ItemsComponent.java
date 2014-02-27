package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.WallItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.WiredDataInstance;
import com.cometproject.server.network.messages.outgoing.room.items.RemoveFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.RemoveWallItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastList;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ItemsComponent {
    private Room room;

    private ConcurrentLinkedQueue<FloorItem> floorItems;
    private ConcurrentLinkedQueue<WallItem> wallItems;

    private Logger log;

    public ItemsComponent(Room room) {
        this.room = room;
        this.floorItems = new ConcurrentLinkedQueue<>();
        this.wallItems = new ConcurrentLinkedQueue<>();

        log = Logger.getLogger("Room Items Component [" + room.getData().getName() + "]");
        this.loadItems();
    }

    public void dispose() {
        this.floorItems.clear();
        this.wallItems.clear();
        this.room = null;
        this.log = null;
    }

    private void loadItems() {
        if(floorItems.size() != 0) {
            floorItems.clear();
        }

        try {
            PreparedStatement query = Comet.getServer().getStorage().prepare("SELECT * FROM items WHERE room_id = ?");
            query.setInt(1, room.getId());

            ResultSet data = query.executeQuery();

            while(data.next()) {
                if(data.getString("wall_pos").equals(""))
                    this.getFloorItems().add(new FloorItem(data.getInt("id"), data.getInt("base_item"), this.room.getId(), data.getInt("user_id"), data.getInt("x"), data.getInt("y"), data.getDouble("z"), data.getInt("rot"), data.getString("extra_data")));
                else
                    this.getWallItems().add(new WallItem(data.getInt("id"), data.getInt("base_item"), this.room.getId(), data.getInt("user_id"), data.getString("wall_pos"), data.getString("extra_data")));
            }

        } catch(Exception e) {
            log.error("Error while loading items for room", e);
        }
    }

    public FloorItem addFloorItem(int id, int baseId, int roomId, int ownerId, int x, int y, int rot, double height, String data) {
        FloorItem item = new FloorItem(id, baseId, roomId, ownerId, x, y, height, rot, data);
        this.getFloorItems().add(item);

        return item;
    }

    public WallItem addWallItem(int id, int baseId, int roomId, int ownerId, String position, String data) {
        WallItem item = new WallItem(id, baseId, roomId, ownerId, position, data);
        this.getWallItems().add(item);

        return item;
    }

    public List<FloorItem> getItemsOnSquare(int x, int y) {
        List<FloorItem> items = new ArrayList<>();

        for(FloorItem item : this.getFloorItems()) {
            if(item.getX() == x && item.getY() == y) {
                items.add(item);
            } else {
                List<AffectedTile> AffectedTiles = AffectedTile.getAffectedTilesAt(
                        item.getDefinition().getLength(), item.getDefinition().getWidth(), item.getX(), item.getY(), item.getRotation());

                for(AffectedTile tile : AffectedTiles) {
                    if(x == tile.x && y == tile.y) {
                        if(!items.contains(item)) {
                            items.add(item);
                        }
                    }
                }
            }
        }

        return items;
    }

    public FloorItem getFloorItem(int id) {
        for(FloorItem item : this.getFloorItems()) {
            if(item.getId() == id) {
                return item;
            }
        }

        return null;
    }

    public WallItem getWallItem(int id) {
        for(WallItem item : this.getWallItems()) {
            if(item.getId() == id) {
                return item;
            }
        }

        return null;
    }

    public void removeItem(WallItem item, Session client) {
        Comet.getServer().getStorage().execute("UPDATE items SET wall_pos = '', room_id = 0, user_id = " + client.getPlayer().getId() + " WHERE id = " + item.getId());

        room.getEntities().broadcastMessage(RemoveWallItemMessageComposer.compose(item.getId(), room.getData().getOwnerId()));
        room.getItems().getWallItems().remove(item);

        client.getPlayer().getInventory().add(item.getId(), item.getItemId(), item.getExtraData());
        client.send(UpdateInventoryMessageComposer.compose());
    }

    public void removeItem(FloorItem item, Session client) {
        removeItem(item, client, true);
    }

    public void removeItem(FloorItem item, Session client, boolean toInventory) {
        // the client which is sent here is the removing user (most likely the owner of the room or staff member)
        List<GenericEntity> affectEntities = room.getEntities().getEntitiesAt(item.getX(), item.getY());
        List<Position3D> tilesToUpdate = new FastList<>();

        tilesToUpdate.add(new Position3D(item.getX(), item.getY(), 0d));

        for (GenericEntity entity : affectEntities) {
            if (entity.hasStatus("sit")) {
                entity.removeStatus("sit");
                entity.markNeedsUpdate();
            }
        }

        for (AffectedTile tile : AffectedTile.getAffectedTilesAt(item.getDefinition().getLength(), item.getDefinition().getWidth(), item.getX(), item.getY(), item.getRotation())) {
            List<GenericEntity> affectEntities0 = room.getEntities().getEntitiesAt(tile.x, tile.y);
            tilesToUpdate.add(new Position3D(tile.x, tile.y, 0d));

            for (GenericEntity entity0 : affectEntities0) {
                if (entity0.hasStatus("sit")) {
                    entity0.removeStatus("sit");
                    entity0.markNeedsUpdate();
                }
            }
        }

        room.getEntities().broadcastMessage(RemoveFloorItemMessageComposer.compose(item.getId(), room.getData().getOwnerId()));
        room.getItems().getFloorItems().remove(item);

        if(toInventory) {
            Comet.getServer().getStorage().execute("UPDATE items SET x = 0, y = 0, z = 0, rot = 0, room_id = 0, user_id = " + client.getPlayer().getId() + " WHERE id = " + item.getId());

            client.getPlayer().getInventory().add(item.getId(), item.getItemId(), item.getExtraData());
            client.send(UpdateInventoryMessageComposer.compose());
        } else {
            Comet.getServer().getStorage().execute("DELETE FROM items WHERE id = " + item.getId());
        }

        if(GameEngine.getWired().isWiredItem(item)) {
            WiredDataInstance instance = WiredDataFactory.get(item);
            Comet.getServer().getStorage().execute("DELETE FROM items_wired_data WHERE id = " + instance.getId());
            WiredDataFactory.removeInstance(item.getId());

            instance.dispose();
        }

        for(Position3D tileToUpdate : tilesToUpdate) {
            room.getMapping().updateTile(tileToUpdate.getX(), tileToUpdate.getY());
        }
    }

    public Room getRoom() {
        return this.room;
    }

    public ConcurrentLinkedQueue<FloorItem> getFloorItems() {
        return this.floorItems;
    }

    public ConcurrentLinkedQueue<WallItem> getWallItems() {
        return this.wallItems;
    }
}
