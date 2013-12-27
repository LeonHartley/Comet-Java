package com.cometsrv.game.players.types;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerSettings {
    private int[] volumes;

    private boolean hideOnline;
    private boolean hideInRoom;
    private boolean allowFriendRequests;
    private boolean allowTrade;

    private int homeRoom;

    public PlayerSettings(ResultSet data) throws SQLException {
        String[] vol = data.getString("volume").split(",");
        this.volumes = new int[vol.length];

        for(int i = 0; i < this.volumes.length; i++) {
            this.volumes[i] = Integer.parseInt(vol[i]);
        }

        this.hideOnline = data.getString("hide_online").equals("1");
        this.hideInRoom = data.getString("hide_inroom").equals("1");
        this.allowFriendRequests = data.getString("allow_friend_requests").equals("1");
        this.allowTrade = data.getString("allow_trade").equals("1");

        this.homeRoom = data.getInt("home_room");
    }

    public PlayerSettings() {
        this.volumes = new int[] {100, 100, 100};
        this.hideInRoom = false;
        this.homeRoom = 1;
        this.hideOnline = false;
        this.allowFriendRequests = true;
        this.allowTrade = true;
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
}
