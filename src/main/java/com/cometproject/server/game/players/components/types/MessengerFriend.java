package com.cometproject.server.game.players.components.types;

import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.sql.ResultSet;
import java.sql.SQLException;


public class MessengerFriend {
    private int userId;

    private PlayerData playerData;

    public MessengerFriend(ResultSet data) throws SQLException {
        this.userId = data.getInt("user_two_id");

        if (NetworkManager.getInstance().getSessions().getByPlayerId(userId) != null) {
            Session session = NetworkManager.getInstance().getSessions().getByPlayerId(userId);

            this.playerData = session.getPlayer() == null || session.getPlayer().getData() == null ? PlayerDao.getDataById(userId) : session.getPlayer().getData();
        } else {
            this.playerData = PlayerDao.getDataById(userId);
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

    public PlayerData getData() {
        return this.playerData;
    }

    public boolean isOnline() {
        return PlayerManager.getInstance().isOnline(userId);
    }

    public Session getSession() {
        return NetworkManager.getInstance().getSessions().getByPlayerId(this.userId);
    }
}
