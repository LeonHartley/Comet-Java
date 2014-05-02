package com.cometproject.server.game.moderation.types;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HelpTicket {
    private int id, playerId, reportedId, category, roomId;
    private String state, message;

    public HelpTicket(int id, int playerId, int reportedId, int category, String message, int roomId) {
        this.id = id;
        this.playerId = playerId;
        this.reportedId = reportedId;
        this.roomId = roomId;
        this.category = category;
        this.message = message;
    }

    public HelpTicket(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.playerId = data.getInt("player_id");
        this.reportedId = data.getInt("reported_id");
        this.roomId = data.getInt("room_id");
        this.category = data.getInt("category");
        this.message = data.getString("message");
    }

    public void updateState() {
        //Comet.getServer().getStorage().execute("UPDATE moderation_help_tickets SET state = 'closed' WHERE id = " + this.id);
    }

    public int getId() {
        return this.id;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public int getReportedId() {
        return this.reportedId;
    }

    public int getCategory() {
        return this.category;
    }

    public String getMessage() {
        return this.message;
    }
}
