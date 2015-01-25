package com.cometproject.server.game.players.components.types;

import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.components.types.messenger.MessengerFriendData;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;

import java.sql.ResultSet;
import java.sql.SQLException;


public class MessengerFriend {
    private int userId;

    private MessengerFriendData friendData;

    public MessengerFriend(ResultSet data) throws SQLException {
        this.userId = data.getInt("user_two_id");

        if (NetworkManager.getInstance().getSessions().getByPlayerId(userId) != null) {
            Session session = NetworkManager.getInstance().getSessions().getByPlayerId(userId);

            this.friendData = session.getPlayer() == null || session.getPlayer().getData() == null ? MessengerDao.getFriendDataByPlayerId(userId) : session.getPlayer().getData().toFriendData();
        } else {
            this.friendData = MessengerDao.getFriendDataByPlayerId(userId);
        }
    }

    public MessengerFriend(int userId) {
        this.userId = userId;
    }

    public boolean isInRoom() {
        if (!isOnline()) {
            return false;
        }

        Session client = NetworkManager.getInstance().getSessions().getByPlayerId(this.userId);

        // Could have these in 1 statement, but to make it easier to read - lets just leave it like this. :P
        if (client.getPlayer() == null || client.getPlayer().getEntity() == null) {
            return false;
        }

        if (!client.getPlayer().getEntity().isVisible())
            return false;

        return true;
    }

    public int getUserId() {
        return this.userId;
    }

    public MessengerFriendData getData() {
        return this.friendData;
    }

    public boolean isOnline() {
        return PlayerManager.getInstance().isOnline(userId);
    }

    public Session getSession() {
        return NetworkManager.getInstance().getSessions().getByPlayerId(this.userId);
    }
}
