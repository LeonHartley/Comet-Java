package com.cometproject.api.game.players.data;

import com.cometproject.api.game.players.data.types.IPlaylistItem;
import com.cometproject.api.game.players.data.types.IVolumeData;
import com.cometproject.api.game.players.data.types.IWardrobeItem;

import java.util.List;

public interface IPlayerSettings {

    IVolumeData getVolumes();

    boolean getHideOnline();

    boolean getHideInRoom();

    boolean getAllowFriendRequests();

    void setAllowFriendRequests(boolean allowFriendRequests);

    void setPersonalStaff(boolean b);

    boolean hasPersonalStaff();

    boolean getAllowTrade();
    
    boolean getAllowFollow();
    
    boolean getAllowMimic();

    int getHomeRoom();

    void setHomeRoom(int homeRoom);

    List<IWardrobeItem> getWardrobe();

    void setWardrobe(List<IWardrobeItem> wardrobe);

    List<IPlaylistItem> getPlaylist();

    boolean isUseOldChat();

    void setUseOldChat(boolean useOldChat);

    boolean ignoreEvents();

    void setIgnoreInvites(boolean ignoreInvites);
}
