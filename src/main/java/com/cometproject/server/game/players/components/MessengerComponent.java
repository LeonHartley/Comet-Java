package com.cometproject.server.game.players.components;

import com.cometproject.server.game.players.components.types.messenger.MessengerFriend;
import com.cometproject.server.game.players.components.types.messenger.MessengerRequest;
import com.cometproject.server.game.players.components.types.messenger.MessengerSearchResult;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerComponent;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.messenger.MessengerSearchResultsMessageComposer;
import com.cometproject.server.network.messages.outgoing.messenger.UpdateFriendStateMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;
import com.cometproject.server.storage.queries.player.messenger.MessengerSearchDao;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;


public class MessengerComponent implements PlayerComponent {
    private Player player;
    private Map<Integer, MessengerFriend> friends;
    private List<MessengerRequest> requests;

    public MessengerComponent(Player player) {
        this.player = player;

        try {
            this.friends = MessengerDao.getFriendsByPlayerId(player.getId());
            this.requests = MessengerDao.getRequestsByPlayerId(player.getId());
        } catch (Exception e) {
            Logger.getLogger(MessengerComponent.class.getName()).error("Error while loading messenger friends", e);
        }
    }

    public void dispose() {
        this.sendStatus(false, false);

        this.requests.clear();
        this.friends.clear();
        this.requests = null;
        this.friends = null;
        this.player = null;
    }

    public Composer search(String query) {
        List<MessengerSearchResult> currentFriends = Lists.newArrayList();
        List<MessengerSearchResult> otherPeople = Lists.newArrayList();

        try {
            for (MessengerSearchResult searchResult : MessengerSearchDao.performSearch(query)) {
                if (this.getFriendById(searchResult.getId()) != null) {
                    currentFriends.add(searchResult);
                } else {
                    otherPeople.add(searchResult);
                }
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

        MessengerDao.deleteFriendship(this.player.getId(), userId);
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
            if (!friend.isOnline() || friend.getUserId() == this.getPlayer().getId()) {
                continue;
            }

            Session session = NetworkManager.getInstance().getSessions().getByPlayerId(friend.getUserId());

            if(session != null)
                session.send(msg);
        }
    }

    public void broadcast(List<Integer> friends, Composer msg) {
        for (int friendId : friends) {
            if (friendId == this.player.getId() || !this.friends.containsKey(friendId) || !this.friends.get(friendId).isOnline()) {
                continue;
            }

            MessengerFriend friend = this.friends.get(friendId);

            if (!friend.isOnline() || friend.getUserId() == this.getPlayer().getId()) {
                continue;
            }

            NetworkManager.getInstance().getSessions().getByPlayerId(friend.getUserId()).send(msg);
        }
    }

    public boolean hasRequestFrom(int playerId) {
        for (MessengerRequest messengerRequest : this.requests) {
            if (messengerRequest.getFromId() == playerId)
                return true;
        }

        return false;
    }

    public void clearRequests() {
        this.requests.clear();
    }

    public void sendOffline(MessengerRequest friend, boolean online, boolean inRoom) {
        this.getPlayer().getSession().send(UpdateFriendStateMessageComposer.compose(friend, online, inRoom));
    }

    public void sendStatus(boolean online, boolean inRoom) {
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

    public void removeRequest(MessengerRequest request) {
        this.requests.remove(request);
    }
}
