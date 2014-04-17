package com.cometproject.server.game.players.components.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.players.data.PlayerLoader;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.sessions.Session;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessengerFriend {
    private int userId;

    private Session client;
    private PlayerData userData;

    public MessengerFriend(ResultSet data) throws SQLException {
        this.userId = data.getInt("user_two_id");

        this.updateClient();

        if (client == null) {
            userData = PlayerLoader.loadDataById(userId);
        } else {
            userData = client.getPlayer().getData();
        }
    }

    public MessengerFriend(int userId, Session session) {
        this.userId = userId;
        this.client = session;
        this.userData = session.getPlayer().getData();
    }

    public Session updateClient() {
        this.client = Comet.getServer().getNetwork().getSessions().getByPlayerId(userId);

        return this.client;
    }

    public void serialize(Composer msg) {
        msg.writeInt(userId);
        msg.writeString(this.getData().getUsername());
        msg.writeInt(1);
        msg.writeBoolean(this.client != null);
        msg.writeBoolean(isInRoom());
        msg.writeString(this.getData().getFigure());
        msg.writeInt(0);
        msg.writeString(this.getData().getMotto());
        msg.writeString("");
        msg.writeString("");
        msg.writeBoolean(true);
        msg.writeBoolean(true);
        msg.writeBoolean(false);
        msg.writeBoolean(false);
        msg.writeBoolean(false);
    }

    private boolean isInRoom() {
        if (client == null) {
            return false;
        }

        if (client.getPlayer().getEntity() == null) {
            return false;
        }

        // more checks?

        return true;
    }

    public int getUserId() {
        return this.userId;
    }

    public Session getClient() {
        return this.client;
    }

    public PlayerData getData() {
        return this.userData;
    }
}
