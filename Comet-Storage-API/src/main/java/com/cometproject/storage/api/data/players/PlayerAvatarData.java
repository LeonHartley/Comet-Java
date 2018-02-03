package com.cometproject.storage.api.data.players;

import com.cometproject.api.game.players.data.PlayerAvatar;

public class PlayerAvatarData implements PlayerAvatar {
    private final int id;
    private String username;
    private String figure;
    private String motto;
    private String gender;

    public PlayerAvatarData(int id, String username, String figure, String motto, String gender) {
        this.id = id;
        this.username = username;
        this.figure = figure;
        this.motto = motto;
        this.gender = gender;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getFigure() {
        return figure;
    }

    @Override
    public void setFigure(String figure) {
        this.figure = figure;
    }

    @Override
    public String getMotto() {
        return motto;
    }

    @Override
    public void setMotto(String motto) {
        this.motto = motto;
    }

    @Override
    public String getGender() {
        return gender;
    }

    @Override
    public void setGender(String gender) {
        this.gender = gender;
    }
}
