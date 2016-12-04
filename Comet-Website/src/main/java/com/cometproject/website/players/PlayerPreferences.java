package com.cometproject.website.players;

import com.cometproject.website.storage.dao.players.PlayerDao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerPreferences {
    private int playerId;
    private boolean showOnlineStatus;
    private PlayerFollowMode followMode;
    private boolean allowFriendRequests;

    public PlayerPreferences(ResultSet data) throws SQLException {
        this.playerId = data.getInt("player_id");
        this.showOnlineStatus = !data.getBoolean("hide_online");
        this.followMode = PlayerFollowMode.valueOf(data.getString("follow_friend_mode").toUpperCase());
        this.allowFriendRequests = data.getBoolean("allow_friend_requests");
    }

    public void save() {
        PlayerDao.savePreferences(this);
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public boolean showOnlineStatus() {
        return showOnlineStatus;
    }

    public void setShowOnlineStatus(boolean showOnlineStatus) {
        this.showOnlineStatus = showOnlineStatus;
    }

    public PlayerFollowMode getFollowMode() {
        return followMode;
    }

    public void setFollowMode(PlayerFollowMode followMode) {
        this.followMode = followMode;
    }

    public boolean allowFriendRequests() {
        return allowFriendRequests;
    }

    public void setAllowFriendRequests(boolean allowFriendRequests) {
        this.allowFriendRequests = allowFriendRequests;
    }

}
