package com.cometproject.api.game.players.data;

import com.cometproject.api.game.players.data.types.IPlaylistItem;
import com.cometproject.api.game.players.data.types.IVolumeData;
import com.cometproject.api.game.players.data.types.IWardrobeItem;

import java.util.List;

public interface IPlayerSettings {

    public IVolumeData getVolumes();

    public boolean getHideOnline();

    public boolean getHideInRoom();

    public boolean getAllowFriendRequests();

    public void setAllowFriendRequests(boolean allowFriendRequests);

    public boolean getAllowTrade();

    public int getHomeRoom();

    public void setHomeRoom(int homeRoom);

    public List<IWardrobeItem> getWardrobe();

    public void setWardrobe(List<IWardrobeItem> wardrobe);

    public List<IPlaylistItem> getPlaylist();

    public boolean isUseOldChat();

    public void setUseOldChat(boolean useOldChat);

    public boolean isIgnoreInvites();

    public void setIgnoreInvites(boolean ignoreInvites);
}
