package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.players.components.types.MessengerRequest;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.messenger.FriendRequestMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestFriendshipMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String username = msg.readString();

        if (username.equals(client.getPlayer().getData().getUsername()))
            return;

        Session request = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        try {
            MessengerRequest req = new MessengerRequest(client.getPlayer().getId(), client.getPlayer().getData().getUsername(), client.getPlayer().getData().getFigure(), client.getPlayer().getData().getMotto());

            if (request != null) {
                request.getPlayer().getMessenger().addRequest(req);
                request.send(FriendRequestMessageComposer.compose(req));
            }

            PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT into `messenger_requests` (`from_id`, `to_id`) VALUES(?, ?);");

            statement.setInt(1, client.getPlayer().getId());

            int userId = 0;

            if (request == null) {
                PreparedStatement std = Comet.getServer().getStorage().prepare("SELECT `id` FROM players WHERE `username` = ?");

                std.setString(1, username);

                ResultSet data = std.executeQuery();

                while (data.next()) {
                    userId = data.getInt("id");
                }
            }

            if (userId == 0)
                return;

            statement.setInt(2, userId);

            statement.execute();
        } catch (SQLException e) {
            GameEngine.getLogger().error("Error while requesting friendship", e);
        }
    }
}
