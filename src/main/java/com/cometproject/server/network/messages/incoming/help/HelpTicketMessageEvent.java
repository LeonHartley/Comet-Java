package com.cometproject.server.network.messages.incoming.help;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.moderation.types.HelpTicket;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.help.TicketSentMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelpTicketMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        boolean hasActiveTicket = GameEngine.getModeration().getTicketByUserId(client.getPlayer().getId()) == null;

        if(hasActiveTicket) {
            return;
        }

        String message = msg.readString();
        int junk = msg.readInt();
        int category = msg.readInt();
        int reportedId = msg.readInt();

        try {
            PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT into moderation_help_tickets (`state`, `player_id`, `reported_id`, `category`, `message`) VALUES(?, ?, ?, ?, ?);");

            statement.setString(1, "open");
            statement.setInt(2, client.getPlayer().getId());
            statement.setInt(3, reportedId);
            statement.setInt(4, category);
            statement.setString(5, message);

            statement.executeQuery();

            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next()) {
                GameEngine.getModeration().addTicket(new HelpTicket(keys.getInt(1), client.getPlayer().getId(), reportedId, category, message));
            }
        } catch (SQLException e) {
            GameEngine.getLogger().error("Error while inserting help ticket", e);
        }

        client.send(TicketSentMessageComposer.compose());
    }
}
