package com.cometproject.server.storage.cache.objects.items;

import com.cometproject.server.game.items.rares.LimitedEditionItemData;

public class WallItemDataObject extends RoomItemDataObject {
    private final String wallPosition;

    public WallItemDataObject(long id, int itemDefinitionId, int roomId, int owner, String ownerName, String data, String wallPosition, LimitedEditionItemData limitedEditionItemData) {
        super(id, itemDefinitionId, roomId, owner, ownerName, data, limitedEditionItemData);

        this.wallPosition = wallPosition;
    }

    public String getWallPosition() {
        return wallPosition;
    }
}
