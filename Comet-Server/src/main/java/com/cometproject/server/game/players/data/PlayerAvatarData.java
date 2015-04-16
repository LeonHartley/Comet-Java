package com.cometproject.server.game.players.data;

public class PlayerAvatarData implements PlayerAvatar {

    private int id;
    private String username;
    private String figure;
    private String motto;

    public PlayerAvatarData(int id, String username, String figure) {
        this.id = id;
        this.username = username;
        this.figure = figure;
    }

    public PlayerAvatarData(int id, String username, String figure, String motto) {
        this.id = id;
        this.username = username;
        this.figure = figure;
        this.motto = motto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }
}
