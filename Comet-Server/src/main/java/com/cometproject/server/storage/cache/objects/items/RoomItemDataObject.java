package com.cometproject.server.storage.cache.objects.items;

import com.cometproject.server.game.items.rares.LimitedEditionItemData;
import com.cometproject.server.storage.cache.CachableObject;

public abstract class RoomItemDataObject extends CachableObject {
    private final long id;
    private final int itemDefinitionId;
    private final int roomId;
    private final int owner;
    private final String ownerName;
    private final String data;

    private final LimitedEditionItemData limitedEditionItemData;

    public RoomItemDataObject(long id, int itemDefinitionId, int roomId, int owner, String ownerName, String data, LimitedEditionItemData limitedEditionItemData) {
        this.id = id;
        this.itemDefinitionId = itemDefinitionId;
        this.roomId = roomId;
        this.owner = owner;
        this.ownerName = ownerName;
        this.data = data;
        this.limitedEditionItemData = limitedEditionItemData;
    }

    public long getId() {
        return id;
    }

    public int getItemDefinitionId() {
        return itemDefinitionId;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getOwner() {
        return owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getData() {
        return data;
    }

    public LimitedEditionItemData getLimitedEditionItemData() {
        return limitedEditionItemData;
    }
}
