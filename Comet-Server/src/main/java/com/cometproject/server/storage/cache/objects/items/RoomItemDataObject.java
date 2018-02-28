package com.cometproject.server.storage.cache.objects.items;

import com.cometproject.api.game.rooms.objects.data.LimitedEditionItemData;
import com.cometproject.server.storage.cache.CachableObject;
import com.google.gson.JsonObject;

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

    public JsonObject toJsonObject() {
        final JsonObject jsonObject = new JsonObject();
        final JsonObject limitedItemData = new JsonObject();

        jsonObject.addProperty("id", this.id);
        jsonObject.addProperty("itemDefinitionId", this.itemDefinitionId);
        jsonObject.addProperty("roomId", this.roomId);
        jsonObject.addProperty("owner", this.owner);
        jsonObject.addProperty("ownerName", this.ownerName);
        jsonObject.addProperty("data", this.data);

        if (this.limitedEditionItemData != null) {
            limitedItemData.addProperty("itemId", this.limitedEditionItemData.getItemId());
            limitedItemData.addProperty("limitedRare", this.limitedEditionItemData.getLimitedRare());
            limitedItemData.addProperty("limitedRareTotal", this.limitedEditionItemData.getLimitedRareTotal());
        }

        jsonObject.add("limitedEditionItemData", this.limitedEditionItemData == null ? null : limitedItemData);

        return jsonObject;
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
