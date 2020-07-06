package com.cometproject.api.game.rooms.objects;

import com.cometproject.api.game.furniture.types.LimitedEditionItem;
import com.cometproject.api.game.rooms.objects.data.ItemData;

import java.util.Optional;

public class RoomItemState {
    private final int id;
    private final int definitionId;
    private final int x;
    private final int y;
    private final int direction;
    private final String z;
    private final String sizeZ;
    private final int extra;
    private final ItemData data;
    private final int expiryTime;
    private final int usagePolicy;
    private final int ownerId;
    private final String ownerName;
    private final LimitedEditionItem limitedEditionItem;

    public RoomItemState(int id, int definitionId, int x, int y, int direction, String z, String sizeZ,
                         int extra, ItemData data, int expiryTime, int usagePolicy, int ownerId, String ownerName,
                         LimitedEditionItem limitedEditionItem) {
        this.id = id;
        this.definitionId = definitionId;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.z = z;
        this.sizeZ = sizeZ;
        this.extra = extra;
        this.data = data;
        this.expiryTime = expiryTime;
        this.usagePolicy = usagePolicy;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.limitedEditionItem = limitedEditionItem;
    }

    public int getId() {
        return id;
    }

    public int getDefinitionId() {
        return definitionId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    public String getZ() {
        return z;
    }

    public String getSizeZ() {
        return sizeZ;
    }

    public ItemData getData() {
        return data;
    }

    public int getUsagePolicy() {
        return usagePolicy;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public Optional<String> getOwnerName() {
        return ownerName == null ? Optional.empty() : Optional.of(ownerName);
    }

    public Optional<LimitedEditionItem> getLimitedEditionData() {
        return limitedEditionItem == null ? Optional.empty() : Optional.of(limitedEditionItem);
    }

    public int getExpiryTime() {
        return expiryTime;
    }

    public int getExtra() {
        return extra;
    }
}
