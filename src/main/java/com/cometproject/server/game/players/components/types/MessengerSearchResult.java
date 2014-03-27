package com.cometproject.server.game.players.components.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.types.Composer;

public class MessengerSearchResult {
    private int id;
    private String username;
    private String figure;
    private String motto;
    private String lastOnline;

    public MessengerSearchResult(int id, String username, String figure, String motto, String lastOnline) {
        this.id = id;
        this.username = username;
        this.figure = figure;
        this.motto = motto;
        this.lastOnline = lastOnline;
    }

    public void compose(Composer msg) {
        msg.writeInt(id);
        msg.writeString(username);
        msg.writeString(motto);
        msg.writeBoolean(Comet.getServer().getNetwork().getSessions().isPlayerLogged(id)); // is online
        msg.writeBoolean(false);
        msg.writeString("");
        msg.writeInt(0);
        msg.writeString(figure);
        msg.writeString(lastOnline);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFigure() {
        return figure;
    }

    public String getMotto() {
        return motto;
    }

    public String getLastOnline() {
        return lastOnline;
    }
}
