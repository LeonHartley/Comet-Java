package com.cometproject.server.game.players.types;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.players.components.*;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.HotelViewMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.CurrenciesMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.purse.SendCreditsMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.groups.GroupDao;
import com.cometproject.server.storage.queries.player.PlayerDao;
import javolution.util.FastMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Player {
    private int id;

    private PlayerSettings settings;
    private PlayerData data;
    private PlayerStatistics stats;

    private PlayerEntity avatar;
    private Session session;

    private PermissionComponent permissions;
    private InventoryComponent inventory;
    private SubscriptionComponent subscription;
    private MessengerComponent messenger;
    private RelationshipComponent relationships;
    private BotComponent bots;
    private PetComponent pets;

    private List<Integer> rooms = new ArrayList<>();
    private List<Integer> groups = new ArrayList<>();

    private List<Integer> ignoredPlayers = new ArrayList<>();

    private long lastMessageTime = 0;
    private double floodTime = 0;
    private int floodFlag = 0;
    private int teleportId = 0;
    private String lastMessage = "";
    private int notifCooldown = 0;

    public Player(ResultSet data, boolean isFallback) throws SQLException {
        this.id = data.getInt("playerId");

        if(isFallback) {
            this.settings = PlayerDao.getSettingsById(this.id);
            this.stats = PlayerDao.getStatisticsById(this.id);
        } else {
            this.settings = new PlayerSettings(data, true);
            this.stats = new PlayerStatistics(data, true);
        }

        this.data = new PlayerData(data);

        this.permissions = new PermissionComponent(this);
        this.inventory = new InventoryComponent(this);
        this.messenger = new MessengerComponent(this);
        this.subscription = new SubscriptionComponent(this);
        this.relationships = new RelationshipComponent(this);
        this.bots = new BotComponent(this);
        this.pets = new PetComponent(this);

        this.groups = GroupDao.getIdsByPlayerId(this.id);

        this.avatar = null;
    }

    public void dispose() {
        if (this.getEntity() != null) {
            this.getEntity().leaveRoom(true, false, false);
        }

        this.getPets().dispose();
        this.getBots().dispose();
        this.getInventory().dispose();
        this.getMessenger().dispose();
        this.getRelationships().dispose();

        this.session.getLogger().debug(this.getData().getUsername() + " logged out");

        PlayerDao.updatePlayerStatus(this, false, false);

        this.rooms.clear();
        this.rooms = null;

        this.groups.clear();
        this.groups = null;

        this.ignoredPlayers.clear();
        this.ignoredPlayers = null;

        this.settings = null;
        this.data = null;
    }

    public void sendBalance() {
        session.send(composeCurrenciesBalance());
        session.send(composeCreditBalance());
    }

    public Composer composeCreditBalance() {
        return SendCreditsMessageComposer.compose(session.getPlayer().getData().getCredits());
    }

    public Composer composeCurrenciesBalance() {
        Map<Integer, Integer> currencies = new FastMap<>();

        currencies.put(0, getData().getActivityPoints());
        currencies.put(105, getData().getVipPoints());

        try {
            return CurrenciesMessageComposer.compose(currencies);
        } finally {
            currencies.clear();
        }
    }

    public void loadRoom(int id, String password) {
        if (avatar != null && avatar.getRoom() != null) {
            avatar.leaveRoom(true, false, false);
            setEntity(null);
        }

        Room room = CometManager.getRooms().get(id);

        if (room == null) {
            session.send(HotelViewMessageComposer.compose());
            return;
        }

        if (room.getEntities() == null) {
            return;
        }

        if (room.getEntities().getEntityByPlayerId(this.id) != null) {
            room.getEntities().getEntityByPlayerId(this.id).leaveRoom(true, false, false);
        }

        PlayerEntity playerEntity = room.getEntities().createEntity(this);
        setEntity(playerEntity);

        playerEntity.joinRoom(room, password);
    }
    
    public void poof() {
        // poof
        if (this.getEntity() != null && this.getEntity().getRoom() != null && this.getEntity().getRoom().getEntities() != null) {
            this.getEntity().unIdle();
            this.getEntity().getRoom().getEntities().broadcastMessage(UpdateInfoMessageComposer.compose(this.getEntity()));
        }

        this.getSession().send(UpdateInfoMessageComposer.compose(-1, this.data.getFigure(), this.data.getGender(), this.getData().getMotto(), this.getData().getAchievementPoints()));
    }

    public void ignorePlayer(int playerId) {
        this.ignoredPlayers.add(playerId);
    }

    public void unignorePlayer(int playerId) {
        this.ignoredPlayers.remove((Integer) playerId);
    }

    public boolean ignores(int playerId) {
        return this.ignoredPlayers.contains(playerId);
    }

    public List<Integer> getRooms() {
        return rooms;
    }

    public void setRooms(List<Integer> rooms) {
        this.rooms = rooms;
    }

    public void setSession(Session client) {
        this.session = client;
    }

    public void setEntity(PlayerEntity avatar) {
        this.avatar = avatar;
    }

    public PlayerEntity getEntity() {
        return this.avatar;
    }

    public Session getSession() {
        return this.session;
    }

    public PlayerData getData() {
        return this.data;
    }

    public PlayerStatistics getStats() {
        return this.stats;
    }

    public PermissionComponent getPermissions() {
        return this.permissions;
    }

    public MessengerComponent getMessenger() {
        return this.messenger;
    }

    public InventoryComponent getInventory() {
        return this.inventory;
    }

    public SubscriptionComponent getSubscription() {
        return this.subscription;
    }

    public RelationshipComponent getRelationships() {
        return this.relationships;
    }

    public BotComponent getBots() {
        return this.bots;
    }

    public PetComponent getPets() {
        return this.pets;
    }

    public PlayerSettings getSettings() {
        return this.settings;
    }

    public int getId() {
        return this.id;
    }

    public boolean isTeleporting() {
        return this.teleportId != 0;
    }

    public int getTeleportId() {
        return this.teleportId;
    }

    public void setTeleportId(int teleportId) {
        this.teleportId = teleportId;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public double getFloodTime() {
        return floodTime;
    }

    public void setFloodTime(double floodTime) {
        this.floodTime = floodTime;
    }

    public int getFloodFlag() {
        return floodFlag;
    }

    public void setFloodFlag(int floodFlag) {
        this.floodFlag = floodFlag;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<Integer> getGroups() {
        return groups;
    }

    public int getNotifCooldown() {
        return this.notifCooldown;
    }

    public void setNotifCooldown(int notifCooldown) {
        this.notifCooldown = notifCooldown;
    }
}
