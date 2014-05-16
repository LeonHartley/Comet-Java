package com.cometproject.server.game.players.types;

import com.cometproject.server.game.players.components.types.PlaylistItem;
import com.cometproject.server.game.players.components.types.WardrobeItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerSettings {
    private int[] volumes;

    private List<WardrobeItem> wardrobe;
    private List<PlaylistItem> playlist;

    private boolean hideOnline;
    private boolean hideInRoom;
    private boolean allowFriendRequests;
    private boolean allowTrade;

    private int homeRoom;

    public PlayerSettings(ResultSet data) throws SQLException {
        String[] vol = data.getString("volume").split(",");
        this.volumes = new int[vol.length];

        for (int i = 0; i < this.volumes.length; i++) {
            this.volumes[i] = Integer.parseInt(vol[i]);
        }

        this.hideOnline = data.getString("hide_online").equals("1");
        this.hideInRoom = data.getString("hide_inroom").equals("1");
        this.allowFriendRequests = data.getString("allow_friend_requests").equals("1");
        this.allowTrade = data.getString("allow_trade").equals("1");

        this.homeRoom = data.getInt("home_room");

        String wardrobeText = data.getString("wardrobe");

        if(wardrobeText == null || wardrobeText.isEmpty()) {
            wardrobe = new ArrayList<>();
        } else {
            wardrobe = new Gson().fromJson(wardrobeText, new TypeToken<ArrayList<WardrobeItem>>(){}.getType());
        }

        String playlistText = data.getString("playlist");

        if(playlistText == null || playlistText.isEmpty()) {
            playlist = new ArrayList<>();
        } else {
            playlist = new Gson().fromJson(playlistText, new TypeToken<ArrayList<PlaylistItem>>(){}.getType());
        }
    }

    public PlayerSettings() {
        this.volumes = new int[]{100, 100, 100};
        this.hideInRoom = false;
        this.homeRoom = 0;
        this.hideOnline = false;
        this.allowFriendRequests = true;
        this.allowTrade = true;
        this.wardrobe = new ArrayList<>();
        this.playlist = new ArrayList<>();
    }

    public int[] getVolumes() {
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

    public boolean getAllowTrade() {
        return this.allowTrade;
    }

    public int getHomeRoom() {
        return this.homeRoom;
    }

    public void setHomeRoom(int homeRoom) {
        this.homeRoom = homeRoom;
    }

    public List<WardrobeItem> getWardrobe() {
        return wardrobe;
    }

    public void setWardrobe(List<WardrobeItem> wardrobe) {
        this.wardrobe = wardrobe;
    }

    public List<PlaylistItem> getPlaylist() {
        return playlist;
    }
}
