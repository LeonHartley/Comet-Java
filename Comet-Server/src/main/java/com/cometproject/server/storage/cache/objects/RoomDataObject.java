package com.cometproject.server.storage.cache.objects;

import com.cometproject.server.game.bots.BotData;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.objects.entities.types.data.PlayerBotData;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.storage.cache.CachableObject;
import com.cometproject.server.storage.cache.objects.items.FloorItemDataObject;
import com.cometproject.server.storage.cache.objects.items.WallItemDataObject;

import java.util.List;

public class RoomDataObject extends CachableObject {
    private final long id;
    private final RoomData data;
    private final List<Integer> rights;

    private final List<FloorItemDataObject> floorItems;
    private final List<WallItemDataObject> wallItems;

    private final List<PetData> pets;
    private final List<BotData> bots;

    public RoomDataObject(long id, RoomData data, List<Integer> rights, List<FloorItemDataObject> floorItems, List<WallItemDataObject> wallItems, List<PetData> pets, List<BotData> bots) {
        this.id = id;
        this.data = data;
        this.rights = rights;
        this.floorItems = floorItems;
        this.wallItems = wallItems;
        this.pets = pets;
        this.bots = bots;
    }

    public long getId() {
        return id;
    }

    public RoomData getData() {
        return data;
    }

    public List<Integer> getRights() {
        return rights;
    }

    public List<FloorItemDataObject> getFloorItems() {
        return floorItems;
    }

    public List<WallItemDataObject> getWallItems() {
        return wallItems;
    }

    public List<PetData> getPets() {
        return pets;
    }

    public List<BotData> getBots() {
        return bots;
    }
}
