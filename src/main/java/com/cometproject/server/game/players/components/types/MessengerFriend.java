package com.cometproject.server.game.players.components.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessengerFriend {
    private int userId;

    private PlayerData playerData;

    public MessengerFriend(ResultSet data) throws SQLException {
        this.userId = data.getInt("user_two_id");

        if(Comet.getServer().getNetwork().getSessions().getByPlayerId(userId) != null) {
            Session session = Comet.getServer().getNetwork().getSessions().getByPlayerId(userId);

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

        Session client = Comet.getServer().getNetwork().getSessions().getByPlayerId(this.userId);

        if (client.getPlayer() == null || client.getPlayer().getEntity() == null) {
            return false;
        }

        return true;
    }

    public int getUserId() {
        return this.userId;
    }

    public PlayerData getData() {
        return this.playerData;
    }

    public boolean isOnline() {
        return CometManager.getPlayers().isOnline(userId);
    }

    public Session getSession() {
        return Comet.getServer().getNetwork().getSessions().getByPlayerId(this.userId);
    }
}
