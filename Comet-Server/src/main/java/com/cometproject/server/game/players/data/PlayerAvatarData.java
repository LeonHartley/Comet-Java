package com.cometproject.server.game.players.data;

import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.server.game.utilities.validator.PlayerFigureValidator;

public class PlayerAvatarData implements PlayerAvatar {

    private int id;
    private String username;
    private String figure;
    private String gender;
    private String motto;

    private Object tempData = null;

    public PlayerAvatarData(int id, String username, String figure, String gender, String motto) {
        this.id = id;
        this.username = username;
        this.figure = figure;
        this.gender = gender;
        this.motto = motto;

        if (figure == null) {
            return;
        }

        if (!PlayerFigureValidator.isValidFigureCode(this.figure, gender)) {
            this.figure = PlayerData.DEFAULT_FIGURE;
        }
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

    @Override
    public String getGender() {
        return this.gender;
    }

    @Override
    public void setGender(String gender) {
        this.gender = gender;
    }

    public void tempData(final Object data) {
        this.tempData = data;
    }

    public Object tempData() {
        return this.tempData;
    }

}
