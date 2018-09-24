package com.cometproject.server.game.players.components;

import com.cometproject.api.game.achievements.types.AchievementType;
import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.game.players.data.components.PlayerMessenger;
import com.cometproject.api.game.players.data.components.messenger.IMessengerFriend;
import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.players.components.types.messenger.MessengerSearchResult;
import com.cometproject.server.game.players.types.Player;
import com.cometproject.server.game.players.types.PlayerComponent;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.messenger.BuddyListMessageComposer;
import com.cometproject.server.network.messages.outgoing.messenger.FriendRequestsMessageComposer;
import com.cometproject.server.network.messages.outgoing.messenger.MessengerSearchResultsMessageComposer;
import com.cometproject.server.network.messages.outgoing.messenger.UpdateFriendStateMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;
import com.cometproject.server.storage.queries.player.messenger.MessengerSearchDao;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;


public class MessengerComponent extends PlayerComponent implements PlayerMessenger {
    private Map<Integer, IMessengerFriend> friends;

    private List<Integer> requests;
    private boolean initialised;

    public MessengerComponent(IPlayer player) {
        super(player);

        try {
            this.friends = MessengerDao.getFriendsByPlayerId(player.getId());
        } catch (Exception e) {
            Logger.getLogger(MessengerComponent.class.getName()).error("Error while loading messenger friends", e);
        }
    }

    public void dispose() {
        this.sendStatus(false, false);

        if (this.getRequests() != null) {
            this.requests.clear();
        }

        this.friends.clear();
        this.requests = null;
        this.friends = null;
    }

    @Override
    public IMessageComposer search(String query) {
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
            this.getPlayer().getSession().getLogger().error("Error while searching for players", e);
        }

        return new MessengerSearchResultsMessageComposer(currentFriends, otherPeople);
    }

    @Override
    public void addRequest(int playerId) {
        this.getRequests().add(playerId);

        this.getPlayer().flush();
    }

    @Override
    public void addFriend(IMessengerFriend friend) {
        if(this.getFriends().containsKey(friend.getUserId())) {
            return;
        }

        this.getFriends().put(friend.getUserId(), friend);
        this.getPlayer().getAchievements().progressAchievement(AchievementType.FRIENDS_LIST, 1);
    }

    @Override
    public void removeFriend(int userId) {
        if (!this.friends.containsKey(userId)) {
            return;
        }

        this.friends.remove(userId);

        MessengerDao.deleteFriendship(this.getPlayer().getId(), userId);
        this.getPlayer().getSession().send(new UpdateFriendStateMessageComposer(-1, userId));

        this.getPlayer().flush();
    }

    @Override
    public Integer getRequestBySender(int sender) {
        for (Integer request : requests) {
            if (request == sender) {
                return request;
            }
        }

        return null;
    }

    @Override
    public void broadcast(IMessageComposer msg) {
        for (IMessengerFriend friend : this.getFriends().values()) {
            if (!friend.isOnline() || friend.getUserId() == this.getPlayer().getId()) {
                continue;
            }

            Session session = NetworkManager.getInstance().getSessions().getByPlayerId(friend.getUserId());

            if (session != null && session.getPlayer().getMessenger().isInitialised())
                session.send(msg);
        }
    }

    @Override
    public void broadcast(List<Integer> friends, IMessageComposer msg) {
        for (int friendId : friends) {
            if (friendId == this.getPlayer().getId() || !this.friends.containsKey(friendId) || !this.friends.get(friendId).isOnline()) {
                continue;
            }

            IMessengerFriend friend = this.friends.get(friendId);

            if (!friend.isOnline() || friend.getUserId() == this.getPlayer().getId()) {
                continue;
            }

            Session session = NetworkManager.getInstance().getSessions().getByPlayerId(friend.getUserId());

            if (session != null && session.getPlayer() != null)
                session.send(msg);
        }
    }

    @Override
    public boolean hasRequestFrom(int playerId) {
        if (this.requests == null) return false;

        for (Integer messengerRequest : this.requests) {
            if (messengerRequest == playerId)
                return true;
        }

        return false;
    }

    @Override
    public List<PlayerAvatar> getRequestAvatars() {
        List<PlayerAvatar> avatars = Lists.newArrayList();

        for (int playerId : this.getRequests()) {
            PlayerAvatar playerAvatar = PlayerManager.getInstance().getAvatarByPlayerId(playerId, PlayerAvatar.USERNAME_FIGURE);

            if (playerAvatar != null) {
                avatars.add(playerAvatar);
            }
        }

        return avatars;
    }

    @Override
    public void clearRequests() {
        this.requests.clear();

        this.getPlayer().flush();
    }

    @Override
    public void sendOffline(int friend, boolean online, boolean inRoom) {
        this.getPlayer().getSession().send(new UpdateFriendStateMessageComposer(PlayerManager.getInstance().getAvatarByPlayerId(friend, PlayerAvatar.USERNAME_FIGURE_MOTTO), online, inRoom, this.getPlayer().getRelationships().get(friend)));

        this.getPlayer().flush();
    }

    @Override
    public void sendStatus(boolean online, boolean inRoom) {
        if (this.getPlayer() == null || this.getPlayer().getSettings() == null) {
            return;
        }

        if (this.getPlayer().getSettings().getHideOnline()) {
            return;
        }

        for (IMessengerFriend friend : this.getFriends().values()) {
            if (!friend.isOnline() || friend.getUserId() == this.getPlayer().getId()) {
                continue;
            }

            Session session = NetworkManager.getInstance().getSessions().getByPlayerId(friend.getUserId());

            if (session != null && session.getPlayer().getMessenger().isInitialised())
                session.send(new UpdateFriendStateMessageComposer(this.getPlayer().getData(), online, inRoom, session.getPlayer().getRelationships().get(this.getPlayer().getId())));
        }

        this.getPlayer().flush();
    }

    @Override
    public IMessengerFriend getFriendById(int id) {
        return this.getFriends().get(id);
    }

    @Override
    public Map<Integer, IMessengerFriend> getFriends() {
        return this.friends;
    }

    @Override
    public List<Integer> getRequests() {
        if (this.requests == null) {
            this.requests = MessengerDao.getRequestsByPlayerId(this.getPlayer().getId());
        }

        return this.requests;
    }

    @Override
    public void removeRequest(Integer request) {
        this.getRequests().remove(request);
    }

    @Override
    public boolean isInitialised() {
        return initialised;
    }

    @Override
    public void setInitialised(boolean initialised) {
        this.initialised = initialised;
    }

    public void initialise() {
        this.getPlayer().getSession().send(new BuddyListMessageComposer((Player) this.getPlayer(),
                this.getFriends(),
                this.getPlayer().getPermissions().getRank().messengerStaffChat(),
                this.getPlayer().getPermissions().getRank().messengerLogChat(),
                this.getPlayer().getGroups()));

        this.getPlayer().getSession().send(new FriendRequestsMessageComposer(this.getRequestAvatars()));

        if (!this.getPlayer().getAchievements().hasStartedAchievement(AchievementType.FRIENDS_LIST)) {
            this.getPlayer().getAchievements().progressAchievement(AchievementType.FRIENDS_LIST, this.getFriends().size());
        }

        this.setInitialised(true);
    }
}
