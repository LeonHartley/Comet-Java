package com.cometproject.server.network.messages.incoming.messenger;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.players.components.types.MessengerFriend;
import com.cometproject.server.game.players.components.types.MessengerRequest;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;
import javolution.util.FastList;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AcceptFriendshipMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int amount = msg.readInt();
        List<MessengerRequest> requests = new FastList<>();

        for(int i = 0; i < amount; i++) {
            requests.add(client.getPlayer().getMessenger().getRequestBySender(msg.readInt()));
        }

        try {
            for(MessengerRequest request : requests) {
                PreparedStatement statement = Comet.getServer().getStorage().prepare("INSERT into `messenger_friendships` VALUES(?, ?);");

                statement.setInt(1, client.getPlayer().getId());
                statement.setInt(2, request.getFromId());

                statement.execute();

                statement = Comet.getServer().getStorage().prepare("INSERT into `messenger_friendships` VALUES(?, ?);");

                statement.setInt(1, request.getFromId());
                statement.setInt(2, client.getPlayer().getId());

                statement.execute();

                Comet.getServer().getStorage().execute("DELETE FROM messenger_requests WHERE from_id = " + request.getFromId() + " AND to_id = " + client.getPlayer().getId());
                Comet.getServer().getStorage().execute("DELETE FROM messenger_requests WHERE to_id = " + request.getFromId() + " AND from_id = " + client.getPlayer().getId());

                Session friend = Comet.getServer().getNetwork().getSessions().getByPlayerId(request.getFromId());

                if(friend != null) {
                    friend.getPlayer().getMessenger().addFriend(new MessengerFriend(client.getPlayer().getId(), client));
                    friend.getPlayer().getMessenger().sendStatus(true, friend.getPlayer().getEntity() != null);
                } else {
                    client.getPlayer().getMessenger().sendOffline(request, false, false);
                }

                client.getPlayer().getMessenger().addFriend(new MessengerFriend(request.getFromId(), client));
                client.getPlayer().getMessenger().sendStatus(true, client.getPlayer().getEntity() != null);
            }
        } catch(SQLException e) {
            GameEngine.getLogger().error("Error while accepting messenger request", e);
        }
    }
}
