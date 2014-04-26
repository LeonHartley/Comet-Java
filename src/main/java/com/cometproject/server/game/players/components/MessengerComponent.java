package com.cometproject.server.game.players.components;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.players.components.types.MessengerFriend;
import com.cometproject.server.game.players.components.types.MessengerRequest;
import com.cometproject.server.game.players.components.types.MessengerSearchResult;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.network.messages.outgoing.messenger.MessengerSearchResultsMessageComposer;
import com.cometproject.server.network.messages.outgoing.messenger.UpdateFriendStateMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import java.util.ArrayList;
import javolution.util.FastMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MessengerComponent {
    private Player player;
    private Map<Integer, MessengerFriend> friends;
    private List<MessengerRequest> requests;

    public MessengerComponent(Player player) {
        this.player = player;
        this.friends = new FastMap<>();
        this.requests = new ArrayList<>();

        try {
            ResultSet friend = Comet.getServer().getStorage().getTable("SELECT * FROM messenger_friendships WHERE user_one_id = " + player.getId());
            ResultSet request = Comet.getServer().getStorage().getTable("SELECT * FROM messenger_requests WHERE to_id = " + player.getId());

            while (friend.next()) {
                this.friends.put(friend.getInt("user_two_id"), new MessengerFriend(friend));
            }

            while (request.next()) {
                this.requests.add(new MessengerRequest(request));
            }
        } catch (Exception e) {
            player.getSession().getLogger().error("Error while loading messenger friends", e);
        }
    }

    public void dispose() {
        sendStatus(false, false);

        this.requests.clear();
        this.friends.clear();
        this.requests = null;
        this.friends = null;
        this.player = null;
    }

    public Composer search(String query) {
        List<MessengerSearchResult> currentFriends = new ArrayList<>();
        List<MessengerSearchResult> otherPeople = new ArrayList<>();

        try {
            PreparedStatement players = Comet.getServer().getStorage().prepare("SELECT id, username, figure, motto, last_online FROM players WHERE username LIKE ? LIMIT 50;");
            players.setString(1, query + "%");

            ResultSet results = players.executeQuery();

            while (results.next()) {
                if (this.getFriendById(results.getInt("id")) != null)
                    currentFriends.add(new MessengerSearchResult(results.getInt("id"), results.getString("username"), results.getString("figure"), results.getString("motto"), new Date(results.getInt("last_online") * 1000L).toString()));
                else
                    otherPeople.add(new MessengerSearchResult(results.getInt("id"), results.getString("username"), results.getString("figure"), results.getString("motto"), new Date(results.getInt("last_online") * 1000L).toString()));
            }

        } catch (Exception e) {
            player.getSession().getLogger().error("Error while searching for players", e);
        }

        return MessengerSearchResultsMessageComposer.compose(currentFriends, otherPeople);
    }

    public void addRequest(MessengerRequest request) {
        this.getRequests().add(request);
    }

    public void addFriend(MessengerFriend friend) {
        this.getFriends().put(friend.getUserId(), friend);
    }


    public void removeFriend(int userId) {
        if (!this.friends.containsKey(userId)) {
            return;
        }

        this.friends.remove(userId);

        Comet.getServer().getStorage().execute("DELETE from messenger_friendships WHERE user_one_id = " + this.player.getId() + " AND user_two_id = " + userId);
        this.player.getSession().send(UpdateFriendStateMessageComposer.compose(-1, userId));
    }

    public MessengerRequest getRequestBySender(int sender) {
        for (MessengerRequest request : requests) {
            if (request.getFromId() == sender) {
                return request;
            }
        }

        return null;
    }

    public void broadcast(Composer msg) {
        for (MessengerFriend friend : this.getFriends().values()) {
            if (friend.getClient() == null || friend.getClient().getPlayer() == null || friend.getUserId() == this.getPlayer().getId()) {
                continue;
            }

            friend.getClient().send(msg);
        }
    }

    public void broadcast(List<Integer> friends, Composer msg) {
        for (int friendId : friends) {
            if (friendId == this.player.getId() || !this.friends.containsKey(friendId) || this.friends.get(friendId).updateClient() == null)
                continue;

            MessengerFriend friend = this.friends.get(friendId);

            if (friend.getClient().getPlayer() == null) {
                continue;
            }

            friend.getClient().send(msg);
        }
    }

    public void sendOffline(MessengerRequest friend, boolean online, boolean inRoom) {
        for (MessengerFriend f : this.getFriends().values()) {
            f.updateClient();
        }

        this.getPlayer().getSession().send(UpdateFriendStateMessageComposer.compose(friend, online, inRoom));
    }

    public void sendStatus(boolean online, boolean inRoom) {
        for (MessengerFriend f : this.getFriends().values()) {
            f.updateClient();
        }

        this.broadcast(UpdateFriendStateMessageComposer.compose(this.getPlayer().getData(), online, inRoom));
    }

    public MessengerFriend getFriendById(int id) {
        return this.getFriends().get(id);
    }

    public Map<Integer, MessengerFriend> getFriends() {
        return this.friends;
    }

    public List<MessengerRequest> getRequests() {
        return this.requests;
    }

    public Player getPlayer() {
        return this.player;
    }
}
