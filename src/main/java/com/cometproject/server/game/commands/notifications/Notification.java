package com.cometproject.server.game.commands.notifications;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Notification {
    private String trigger;
    private String text;
    private NotificationType type;
    private int minRank;
    private int coolDown;

    public Notification(ResultSet data) throws SQLException {
        this.trigger = data.getString("name");
        this.text = data.getString("text");
        this.type = NotificationType.valueOf(data.getString("type").toUpperCase());
        this.minRank = data.getInt("min_rank");
        this.coolDown = data.getInt("cooldown");
    }

    public String getTrigger() {
        return trigger;
    }

    public String getText() {
        return text;
    }

    public NotificationType getType() {
        return type;
    }

    public int getMinRank() {
        return minRank;
    }

    public int getCoolDown() {
        return coolDown;
    }
}
