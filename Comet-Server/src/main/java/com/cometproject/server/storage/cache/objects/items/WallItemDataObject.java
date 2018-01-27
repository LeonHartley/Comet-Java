package com.cometproject.server.storage.cache.objects.items;

import com.cometproject.api.game.rooms.objects.data.LimitedEditionItemData;
import com.google.gson.JsonObject;

public class WallItemDataObject extends RoomItemDataObject {
    private final String wallPosition;

    public WallItemDataObject(long id, int itemDefinitionId, int roomId, int owner, String ownerName, String data, String wallPosition, LimitedEditionItemData limitedEditionItemData) {
        super(id, itemDefinitionId, roomId, owner, ownerName, data, limitedEditionItemData);

        this.wallPosition = wallPosition;
    }

    @Override
    public JsonObject toJsonObject() {
        final JsonObject object = super.toJsonObject();

        object.addProperty("wallPosition", this.wallPosition);

        return object;
    }

    public String getWallPosition() {
        return wallPosition;
    }
}
