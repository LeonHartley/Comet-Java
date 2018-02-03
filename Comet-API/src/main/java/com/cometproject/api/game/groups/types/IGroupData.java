package com.cometproject.api.game.groups.types;

import com.cometproject.api.game.players.data.PlayerAvatar;

public interface IGroupData {
    int getId();

    void setId(int id);

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    int getOwnerId();

    void setOwnerId(int id);

    String getBadge();

    void setBadge(String badge);

    int getRoomId();

    void setRoomId(int roomId);

    int getCreatedTimestamp();

    boolean canMembersDecorate();

    void setCanMembersDecorate(boolean canMembersDecorate);

    GroupType getType();

    void setType(GroupType type);

    int getColourA();

    void setColourA(int colourA);

    int getColourB();

    void setColourB(int colourB);

    boolean hasForum();

    void setHasForum(boolean hasForum);

    String getOwnerName();

    PlayerAvatar getOwnerAvatar();
}
