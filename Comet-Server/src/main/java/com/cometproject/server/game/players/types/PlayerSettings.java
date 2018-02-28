package com.cometproject.server.game.players.types;

import com.cometproject.api.game.players.data.IPlayerSettings;
import com.cometproject.api.game.players.data.types.IPlaylistItem;
import com.cometproject.api.game.players.data.types.IVolumeData;
import com.cometproject.api.game.players.data.types.IWardrobeItem;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.game.players.components.types.settings.PlaylistItem;
import com.cometproject.server.game.players.components.types.settings.VolumeData;
import com.cometproject.server.game.players.components.types.settings.WardrobeItem;
import com.google.gson.reflect.TypeToken;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PlayerSettings implements IPlayerSettings {
    private VolumeData volumes;

    private List<IWardrobeItem> wardrobe;
    private List<IPlaylistItem> playlist;

    private boolean hideOnline;
    private boolean hideInRoom;
    private boolean allowFriendRequests;
    private boolean allowTrade;
    private boolean allowFollow;
    private boolean allowMimic;

    private int homeRoom;
    private boolean useOldChat;
    private boolean ignoreInvites;

    private int navigatorX;
    private int navigatorY;
    private int navigatorHeight;
    private int navigatorWidth;
    private boolean navigatorShowSearches;

    private boolean ignoreEvents;

    public PlayerSettings(ResultSet data, boolean isLogin) throws SQLException {
        if (isLogin) {
            String volumeData = data.getString("playerSettings_volume");

            if (volumeData != null && volumeData.startsWith("{")) {
                volumes = JsonUtil.getInstance().fromJson(volumeData, VolumeData.class);
            } else {
                volumes = new VolumeData(100, 100, 100);
            }

            this.hideOnline = data.getString("playerSettings_hideOnline").equals("1");
            this.hideInRoom = data.getString("playerSettings_hideInRoom").equals("1");
            this.allowFriendRequests = data.getString("playerSettings_allowFriendRequests").equals("1");
            this.allowTrade = data.getString("playerSettings_allowTrade").equals("1");
            this.allowFollow = data.getString("playerSettings_allowFollow").equals("1");
            this.allowMimic = data.getString("playerSettings_allowMimic").equals("1");

            this.homeRoom = data.getInt("playerSettings_homeRoom");

            String wardrobeText = data.getString("playerSettings_wardrobe");

            if (wardrobeText == null || wardrobeText.isEmpty()) {
                wardrobe = new ArrayList<>();
            } else {
                wardrobe = JsonUtil.getInstance().fromJson(wardrobeText, new TypeToken<ArrayList<WardrobeItem>>() {
                }.getType());
            }

            String playlistText = data.getString("playerSettings_playlist");

            if (playlistText == null || playlistText.isEmpty()) {
                playlist = new ArrayList<>();
            } else {
                playlist = JsonUtil.getInstance().fromJson(playlistText, new TypeToken<ArrayList<PlaylistItem>>() {
                }.getType());
            }

            this.useOldChat = data.getString("playerSettings_useOldChat").equals("1");
            this.ignoreInvites = data.getString("playerSettings_ignoreInvites").equals("1");

            this.navigatorX = data.getInt("playerSettings_navigatorX");
            this.navigatorY = data.getInt("playerSettings_navigatorY");
            this.navigatorHeight = data.getInt("playerSettings_navigatorHeight");
            this.navigatorWidth = data.getInt("playerSettings_navigatorWidth");

            this.navigatorShowSearches = data.getString("playerSettings_navigatorShowSearches").equals("1");

            this.ignoreEvents = data.getString("playerSettings_ignoreEvents").equalsIgnoreCase("1");
        } else {
            String volumeData = data.getString("volume");

            if (volumeData != null && volumeData.startsWith("{")) {
                volumes = JsonUtil.getInstance().fromJson(volumeData, VolumeData.class);
            } else {
                volumes = new VolumeData(100, 100, 100);
            }

            this.hideOnline = data.getString("hide_online").equals("1");
            this.hideInRoom = data.getString("hide_inroom").equals("1");
            this.allowFriendRequests = data.getString("allow_friend_requests").equals("1");
            this.allowTrade = data.getString("allow_trade").equals("1");
            this.allowFollow = data.getString("allow_follow").equals("1");
            this.allowFollow = data.getString("allow_mimic").equals("1");

            this.homeRoom = data.getInt("home_room");

            String wardrobeText = data.getString("wardrobe");

            if (wardrobeText == null || wardrobeText.isEmpty()) {
                wardrobe = new ArrayList<>();
            } else {
                wardrobe = JsonUtil.getInstance().fromJson(wardrobeText, new TypeToken<ArrayList<WardrobeItem>>() {
                }.getType());
            }

            String playlistText = data.getString("playlist");

            if (playlistText == null || playlistText.isEmpty()) {
                playlist = new ArrayList<>();
            } else {
                playlist = JsonUtil.getInstance().fromJson(playlistText, new TypeToken<ArrayList<PlaylistItem>>() {
                }.getType());
            }

            this.useOldChat = data.getString("chat_oldstyle").equals("1");
            this.ignoreInvites = data.getString("ignore_invites").equals("1");

            this.navigatorX = data.getInt("navigator_x");
            this.navigatorY = data.getInt("navigator_y");
            this.navigatorHeight = data.getInt("navigator_height");
            this.navigatorWidth = data.getInt("navigator_width");

            this.navigatorShowSearches = data.getString("navigator_show_searches").equals("1");

            this.ignoreEvents = data.getString("ignore_events").equals("1");
        }
    }

    public PlayerSettings() {
        this.volumes = new VolumeData(100, 100, 100);
        this.hideInRoom = false;
        this.homeRoom = 0;
        this.hideOnline = false;
        this.allowFriendRequests = true;
        this.allowTrade = true;
        this.allowFollow = true;
        this.allowMimic = true;
        this.wardrobe = new ArrayList<>();
        this.playlist = new ArrayList<>();
        this.useOldChat = false;

        this.navigatorX = 68;
        this.navigatorY = 42;
        this.navigatorWidth = 425;
        this.navigatorHeight = 592;
        this.navigatorShowSearches = false;
    }

    public IVolumeData getVolumes() {
        return this.volumes;
    }

    public boolean getHideOnline() {
        return this.hideOnline;
    }

    public boolean getHideInRoom() {
        return this.hideInRoom;
    }

    public boolean getAllowFriendRequests() {
        return this.allowFriendRequests;
    }

    public void setAllowFriendRequests(boolean allowFriendRequests) {
        this.allowFriendRequests = allowFriendRequests;
    }

    public boolean getAllowTrade() {
        return this.allowTrade;
    }

    public boolean getAllowFollow() {
        return this.allowFollow;
    }

    public boolean getAllowMimic() {
        return this.allowMimic;
    }

    public int getHomeRoom() {
        return this.homeRoom;
    }

    public void setHomeRoom(int homeRoom) {
        this.homeRoom = homeRoom;
    }

    public List<IWardrobeItem> getWardrobe() {
        return wardrobe;
    }

    public void setWardrobe(List<IWardrobeItem> wardrobe) {
        this.wardrobe = wardrobe;
    }

    public List<IPlaylistItem> getPlaylist() {
        return playlist;
    }

    public boolean isUseOldChat() {
        return this.useOldChat;
    }

    public void setUseOldChat(boolean useOldChat) {
        this.useOldChat = useOldChat;
    }

    public boolean ignoreEvents() {
        return ignoreInvites;
    }

    public void setIgnoreInvites(boolean ignoreInvites) {
        this.ignoreInvites = ignoreInvites;
    }

    public int getNavigatorX() {
        return navigatorX;
    }

    public void setNavigatorX(int navigatorX) {
        this.navigatorX = navigatorX;
    }

    public int getNavigatorY() {
        return navigatorY;
    }

    public void setNavigatorY(int navigatorY) {
        this.navigatorY = navigatorY;
    }

    public int getNavigatorHeight() {
        return navigatorHeight;
    }

    public void setNavigatorHeight(int navigatorHeight) {
        this.navigatorHeight = navigatorHeight;
    }

    public int getNavigatorWidth() {
        return navigatorWidth;
    }

    public void setNavigatorWidth(int navigatorWidth) {
        this.navigatorWidth = navigatorWidth;
    }

    public boolean getNavigatorShowSearches() {
        return navigatorShowSearches;
    }

    public void setNavigatorShowSearches(boolean navigatorShowSearches) {
        this.navigatorShowSearches = navigatorShowSearches;
    }
}
