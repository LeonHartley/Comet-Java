package com.cometproject.network.messages.incoming.messenger;

import com.cometproject.boot.Comet;
import com.cometproject.game.GameEngine;
import com.cometproject.game.players.components.types.MessengerRequest;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.messenger.FriendRequestMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RequestFriendshipMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String username = msg.readString();

        if(username == client.getPlayer().getData().getUsername())
            return;

        Session request = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(username);

        if(request == null || request.getPlayer() == null || request.getPlayer().getMessenger() == null)
            return;

        try {
            MessengerRequest req = new MessengerRequest(client.getPlayer().getId(), client.getPlayer().getData().getUsername(), client.getPlayer().getData().getFigure(), client.getPlayer().getData().getMotto());

            request.getPlayer().getMessenger().addRequest(req);
            request.send(FriendRequestMessageComposer.compose(req));

            PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT into `messenger_requests` (`from_id`, `to_id`) VALUES(?, ?);");

            statement.setInt(1, client.getPlayer().getId());
            statement.setInt(2, request.getPlayer().getId());

            statement.execute();
        } catch(SQLException e) {
            GameEngine.getLogger().error("Error while requesting friendship", e);
        }
    }
}
