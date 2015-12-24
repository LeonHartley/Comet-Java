package com.cometproject.server.game.players.data;

import com.cometproject.server.game.utilities.validator.PlayerFigureValidator;

public class PlayerAvatarData implements PlayerAvatar {

    private int id;
    private String username;
    private String figure;
    private String motto;

    public PlayerAvatarData(int id, String username, String figure, String motto) {
        this.id = id;
        this.username = username;
        this.figure = figure;
        this.motto = motto;

        if(figure == null) { return; }

        if(!PlayerFigureValidator.isValidFigureCode(this.figure, "m")) {
            this.figure = PlayerData.DEFAULT_FIGURE;
        }
    }

    public PlayerAvatarData(int id, String username, String figure) {
        this(id, username, figure, null);
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
