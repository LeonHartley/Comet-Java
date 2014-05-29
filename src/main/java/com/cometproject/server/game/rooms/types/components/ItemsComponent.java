package com.cometproject.server.game.rooms.types.components;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.game.rooms.avatars.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFactory;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.items.RoomItemWall;
import com.cometproject.server.game.rooms.items.queue.RoomItemEventQueue;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.wired.data.WiredDataFactory;
import com.cometproject.server.game.wired.data.WiredDataInstance;
import com.cometproject.server.network.messages.outgoing.room.items.RemoveFloorItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.items.RemoveWallItemMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.items.WiredDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import javolution.util.FastTable;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ItemsComponent {
    private Room room;
    private final Logger log;

    private final FastTable<RoomItemFloor> floorItems = new FastTable<RoomItemFloor>().shared();
    private final FastTable<RoomItemWall> wallItems = new FastTable<RoomItemWall>().shared();

    public ItemsComponent(Room room) {
        this.room = room;

        this.log = Logger.getLogger("Room Items Component [" + room.getData().getName() + "]");

        RoomItemDao.getItems(this.room.getId(), this.floorItems, this.wallItems);

        for(RoomItemFloor floorItem : floorItems) {
            floorItem.onLoad();
        }

        for(RoomItemWall wallItem : wallItems) {
            wallItem.onLoad();
        }
    }

    public void dispose() {
        for(RoomItemFloor floorItem : floorItems) {
            floorItem.onUnload();
        }

        for(RoomItemWall wallItem : wallItems) {
            wallItem.onUnload();
        }

        this.floorItems.clear();
        this.wallItems.clear();

        this.room = null;
    }

    public RoomItemFloor addFloorItem(int id, int baseId, int roomId, int ownerId, int x, int y, int rot, double height, String data) {
        RoomItemFloor floor = RoomItemFactory.createFloor(id, baseId, roomId, ownerId, x, y, height, rot, data);
        this.getFloorItems().add(floor);

        return floor;
    }

    public RoomItemWall addWallItem(int id, int baseId, int roomId, int ownerId, String position, String data) {
        RoomItemWall wall = RoomItemFactory.createWall(id, baseId, roomId, ownerId, position, data);
        this.getWallItems().add(wall);

        return wall;
    }

    public List<RoomItemFloor> getItemsOnSquare(int x, int y) {
        List<RoomItemFloor> items = new ArrayList<>();

        for (RoomItemFloor item : this.getFloorItems()) {
            if (item.getX() == x && item.getY() == y) {
                items.add(item);
            } else {
                List<AffectedTile> affectedTiles = AffectedTile.getAffectedTilesAt(
                        item.getDefinition().getLength(), item.getDefinition().getWidth(), item.getX(), item.getY(), item.getRotation());

                for (AffectedTile tile : affectedTiles) {
                    if (x == tile.x && y == tile.y) {
                        if (!items.contains(item)) {
                            items.add(item);
                        }
                    }
                }
            }
        }

        return items;
    }

    public RoomItemFloor getFloorItem(int id) {
        for (RoomItemFloor item : this.getFloorItems()) {
            if (item.getId() == id) {
                return item;
            }
        }

        return null;
    }

    public RoomItemWall getWallItem(int id) {
        for (RoomItemWall item : this.getWallItems()) {
            if (item.getId() == id) {
                return item;
            }
        }

        return null;
    }

    public List<RoomItemFloor> getByInteraction(String interaction) {
        List<RoomItemFloor> items = new ArrayList<>();

        for (RoomItemFloor floorItem : this.floorItems) {
            if (floorItem.getDefinition().getInteraction().equals(interaction)) {
                items.add(floorItem);
            } else if (interaction.contains("%")) {
                if (interaction.startsWith("%") && floorItem.getDefinition().getInteraction().endsWith(interaction.replace("%", ""))) {
                    items.add(floorItem);
                } else if (interaction.endsWith("%") && floorItem.getDefinition().getInteraction().startsWith(interaction.replace("%", ""))) {
                    items.add(floorItem);
                }
            }
        }

        return items;
    }

    public void removeItem(RoomItemWall item, Session client) {
        RoomItemDao.removeItemFromRoom(item.getId(), client.getPlayer().getId());

        room.getEntities().broadcastMessage(RemoveWallItemMessageComposer.compose(item.getId(), room.getData().getOwnerId()));
        room.getItems().getWallItems().remove(item);

        client.getPlayer().getInventory().add(item.getId(), item.getItemId(), item.getExtraData());
        client.send(UpdateInventoryMessageComposer.compose());
        //client.send(InventoryMessageComposer.compose(client.getPlayer().getInventory()));
    }

    public void removeItem(RoomItemFloor item, Session client) {
        removeItem(item, client, true);
    }

    public void removeItem(RoomItemFloor item, Session client, boolean toInventory) {
        List<GenericEntity> affectEntities = room.getEntities().getEntitiesAt(item.getX(), item.getY());
        List<Position3D> tilesToUpdate = new ArrayList<>();

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

        if (toInventory) {
            RoomItemDao.removeItemFromRoom(item.getId(), client.getPlayer().getId());

            client.getPlayer().getInventory().add(item.getId(), item.getItemId(), item.getExtraData());
            client.send(UpdateInventoryMessageComposer.compose());
        } else {
            RoomItemDao.deleteItem(item.getId());
        }

        if (CometManager.getWired().isWiredItem(item)) {
            WiredDataInstance instance = WiredDataFactory.get(item);

            WiredDao.deleteWiredData(item.getId());
            WiredDataFactory.removeInstance(item.getId());

            instance.dispose();
        }

        for (Position3D tileToUpdate : tilesToUpdate) {
            room.getMapping().updateTile(tileToUpdate.getX(), tileToUpdate.getY());
        }
    }

    public Room getRoom() {
        return this.room;
    }

    public Collection<RoomItemFloor> getFloorItems() {
        return this.floorItems;
    }

    public Collection<RoomItemWall> getWallItems() {
        return this.wallItems;
    }
}
