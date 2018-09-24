package com.cometproject.server.game.players.components.types.messenger;

import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.game.players.data.components.messenger.IMessengerFriend;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.data.PlayerAvatarData;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.sessions.Session;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;


public class MessengerFriend implements IMessengerFriend {
    private int userId;
    private PlayerAvatar playerAvatar;

    public MessengerFriend(ResultSet data) throws SQLException {
        this.userId = data.getInt("user_two_id");
        this.playerAvatar = new PlayerAvatarData(this.userId, data.getString("username"), data.getString("figure"), data.getString("gender"), data.getString("motto"));
    }

    public MessengerFriend(int userId, PlayerAvatar playerAvatar) {
        this.userId = userId;
        this.playerAvatar = playerAvatar;
    }

    @Override
    public boolean isInRoom() {
        if (!isOnline()) {
            return false;
        }

        Session client = NetworkManager.getInstance().getSessions().getByPlayerId(this.userId);

        // Could have these in 1 statement, but to make it easier to read - lets just leave it like this. :P
        if (client == null || client.getPlayer() == null || client.getPlayer().getEntity() == null) {
            return false;
        }

        return client.getPlayer().getEntity().isVisible();
    }

    @Override
    public PlayerAvatar getAvatar() {
        if (this.getSession() != null && this.getSession().getPlayer() != null) {
            return this.getSession().getPlayer().getData();
        }

        return this.playerAvatar;
    }

    @Override
    public int getUserId() {
        return this.userId;
    }

    @Override
    public boolean isOnline() {
        return PlayerManager.getInstance().isOnline(userId);
    }

    @Override
    public ISession getSession() {
        return NetworkManager.getInstance().getSessions().getByPlayerId(this.userId);
    }

    @Override
    public JsonObject toJson() {
        final JsonObject coreObject = new JsonObject();
        final JsonObject playerObject = new JsonObject();

        coreObject.addProperty("id", userId);
        coreObject.addProperty("inRoom", isInRoom());
        coreObject.addProperty("online", isOnline());

        playerObject.addProperty("username", playerAvatar.getUsername());
        playerObject.addProperty("figure", playerAvatar.getFigure());
        playerObject.addProperty("motto", playerAvatar.getMotto());
        playerObject.addProperty("gender", playerAvatar.getGender());

        coreObject.add("playerData", playerObject);

        return coreObject;
    }
}
