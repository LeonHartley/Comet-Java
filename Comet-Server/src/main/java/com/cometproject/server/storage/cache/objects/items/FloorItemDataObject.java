package com.cometproject.server.storage.cache.objects.items;

import com.cometproject.api.game.rooms.objects.data.LimitedEditionItemData;
import com.cometproject.api.game.utilities.Position;
import com.google.gson.JsonObject;

public class FloorItemDataObject extends RoomItemDataObject {

    private final Position position;
    private final int rotation;

    public FloorItemDataObject(long id, int itemDefinitionId, int roomId, int owner, String ownerName, String data, Position position, int rotation, LimitedEditionItemData limitedEditionItemData) {
        super(id, itemDefinitionId, roomId, owner, ownerName, data, limitedEditionItemData);

        this.position = position;
        this.rotation = rotation;
    }

    @Override
    public JsonObject toJsonObject() {
        final JsonObject object = super.toJsonObject();
        final JsonObject positionObject = new JsonObject();

        positionObject.addProperty("x", this.position.getX());
        positionObject.addProperty("y", this.position.getY());
        positionObject.addProperty("z", this.position.getZ());

        object.add("position", positionObject);
        object.addProperty("rotation", this.rotation);

        return object;
    }

    public Position getPosition() {
        return position;
    }

    public int getRotation() {
        return rotation;
    }
}
