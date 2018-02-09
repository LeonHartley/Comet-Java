package com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.google.common.collect.Lists;

import java.util.List;


public class WiredAddonUnseenEffect extends RoomItemFloor {
    private List<Long> seenEffects;

    public WiredAddonUnseenEffect(RoomItemData itemData, Room room) {
        super(itemData, room);

        this.seenEffects = Lists.newArrayList();
    }

    public List<Long> getSeenEffects() {
        return seenEffects;
    }
}
