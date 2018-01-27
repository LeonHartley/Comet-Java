package com.cometproject.api.game.rooms.objects.data;

import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.game.rooms.objects.IRoomItemData;
import com.cometproject.api.game.utilities.Position;

public class RoomItemData implements IRoomItemData {
    private final long id;
    private final int itemId;
    private final int ownerId;
    private final String ownerName;

    private String data;

    private int rotation;
    private Position floorPosition;
    private String wallPosition;

    private final LimitedEditionItem limitedEdition;

    public RoomItemData(long id, int itemId, int ownerId, String ownerName, Position position, int rotation, String data, String wallPosition, LimitedEditionItem limitedEditionItem) {
        this.id = id;
        this.itemId = itemId;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.data = data;

        this.rotation = rotation;
        this.floorPosition = position;

        this.wallPosition = wallPosition;

        this.limitedEdition = limitedEditionItem;
    }

    public long getId() {
        return id;
    }

    @Override
    public int getItemId() {
        return this.itemId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public Position getPosition() {
        return this.floorPosition;
    }

    @Override
    public String getWallPosition() {
        return this.wallPosition;
    }

    @Override
    public int getRotation() {
        return this.rotation;
    }

    @Override
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void setData(int data) {
        this.data = "" + data;
    }

    @Override
    public void setPosition(Position position) {
        this.floorPosition = position;
    }

    @Override
    public void setWallPosition(String wallPosition) {
        this.wallPosition = wallPosition;
    }

    @Override
    public LimitedEditionItem  getLimitedEdition() {
        return limitedEdition;
    }
}
