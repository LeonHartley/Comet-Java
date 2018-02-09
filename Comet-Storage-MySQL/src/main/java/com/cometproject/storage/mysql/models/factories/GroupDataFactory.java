package com.cometproject.storage.mysql.models.factories;

import com.cometproject.api.game.groups.types.GroupType;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.storage.mysql.models.GroupData;

public class GroupDataFactory {

    public IGroupData create(int id, String title, String description, String badge, int ownerId, String ownerName,
                             int roomId, int created, GroupType type, int colourA, int colourB,
                             boolean canMembersDecorate, boolean hasForum, PlayerAvatar playerAvatar) {
        return new GroupData(id, title, description, badge, ownerId, ownerName, roomId, created, type, colourA, colourB,
                canMembersDecorate, hasForum, playerAvatar);
    }

    public IGroupData create(String title, String description, String badge, int ownerId,
                             String ownerName, int roomId, int colourA, int colourB, PlayerAvatar playerAvatar) {
        return new GroupData(-1, title, description, badge, ownerId, ownerName, roomId, (int) (System.currentTimeMillis() / 1000), GroupType.REGULAR, colourA, colourB, false, false, playerAvatar);
    }
}
