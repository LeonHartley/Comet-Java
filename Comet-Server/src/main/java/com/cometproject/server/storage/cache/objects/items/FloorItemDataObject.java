package com.cometproject.server.storage.cache.objects.items;

import com.cometproject.server.game.items.rares.LimitedEditionItemData;
import com.cometproject.server.game.rooms.objects.misc.Position;

public class FloorItemDataObject extends RoomItemDataObject {

    private final Position position;
    private final int rotation;

    public FloorItemDataObject(long id, int itemDefinitionId, int roomId, int owner, String ownerName, String data, Position position, int rotation, LimitedEditionItemData limitedEditionItemData) {
        super(id, itemDefinitionId, roomId, owner, ownerName, data, limitedEditionItemData);

        this.position = position;
        this.rotation = rotation;
    }

    public Position getPosition() {
        return position;
    }

    public int getRotation() {
        return rotation;
    }
}
