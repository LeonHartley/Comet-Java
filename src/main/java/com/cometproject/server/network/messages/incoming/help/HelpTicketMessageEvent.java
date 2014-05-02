package com.cometproject.server.network.messages.incoming.help;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class HelpTicketMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        /*boolean hasActiveTicket = GameEngine.getModeration().getTicketByUserId(client.getPlayer().getId()) != null;

        if (hasActiveTicket) {
            client.send(AdvancedAlertMessageComposer.compose(Locale.get("help.ticket.pending.title"), Locale.get("help.ticket.pending.message")));
            return;
        }

        String message = msg.readString();
        int junk = msg.readInt();
        int category = msg.readInt();
        int reportedId = msg.readInt();
        int timestamp = (int) Comet.getTime();
        int roomId = client.getPlayer().getEntity() != null ? client.getPlayer().getEntity().getRoom().getId() : 0;

        try {
            PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT into moderation_help_tickets (`state`, `player_id`, `reported_id`, `room_id`, `category`, `message`, `timestamp_opened`) VALUES(?, ?, ?, ?, ?, ?, ?);", true);

            statement.setString(1, "open");
            statement.setInt(2, client.getPlayer().getId());
            statement.setInt(3, reportedId);
            statement.setInt(4, roomId);
            statement.setInt(5, category);
            statement.setString(6, message);
            statement.setInt(7, timestamp);

            statement.execute();

            ResultSet keys = statement.getGeneratedKeys();

            if (keys.next()) {
                GameEngine.getModeration().addTicket(new HelpTicket(keys.getInt(1), client.getPlayer().getId(), reportedId, category, message, roomId));
            }
        } catch (SQLException e) {
            GameEngine.getLogger().error("Error while inserting help ticket", e);
        }

        client.send(TicketSentMessageComposer.compose());
        */
    }
}