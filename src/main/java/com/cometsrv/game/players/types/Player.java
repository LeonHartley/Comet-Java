package com.cometsrv.game.players.types;

import com.cometsrv.game.players.components.*;
import com.cometsrv.game.players.data.PlayerData;
import com.cometsrv.game.players.data.PlayerLoader;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.outgoing.user.purse.CurrenciesMessageComposer;
import com.cometsrv.network.messages.outgoing.user.purse.SendCreditsMessageComposer;
import com.cometsrv.network.sessions.Session;
import javolution.util.FastMap;

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

    private Map<Integer, Room> rooms = new FastMap<>();

    public long lastMessage = 0;
    public double floodTime = 0;
    public int floodFlag = 0;

    public Player(int id) {
        this.id = id;

        this.settings = PlayerLoader.loadSettings(this.getId());
        this.data = PlayerLoader.loadDataById(this.getId());
        this.stats = PlayerLoader.loadStatistics(this.getId());

        this.permissions = new PermissionComponent(this);
        this.inventory = new InventoryComponent(this);
        this.messenger = new MessengerComponent(this);
        this.subscription = new SubscriptionComponent(this);
        this.relationships = new RelationshipComponent(this);
        this.bots = new BotComponent(this);

        this.avatar = null;
    }

    public void dispose() {
        /*if(this.getEntity() != null) {
            this.getEntity().dispose(true, false, false);
        }*/

        // TODO: Add dispose to the entity

        this.getBots().dispose();
        this.getInventory().dispose();
        this.getMessenger().dispose();
        this.getRelationships().dispose();

        this.session.getLogger().info(this.getData().getUsername() + " logged out");

        this.rooms.clear();
        this.rooms = null;

        this.settings = null;
        this.data = null;
    }

    public void sendBalance() {
        session.send(SendCreditsMessageComposer.compose(session.getPlayer().getData().getCredits()));
        Map<Integer, Integer> currencies = new FastMap<>();

        currencies.put(0, 0); // duckets
        currencies.put(105, session.getPlayer().getData().getPoints());

        session.send(CurrenciesMessageComposer.compose(currencies));
        currencies.clear();
    }

    public Map<Integer, Room> getRooms() {
        return rooms;
    }

    public void setSession(Session client) {
        this.session = client;
    }

    public void setAvatar(PlayerEntity avatar) {
        this.avatar = avatar;
    }

    @Deprecated
    public PlayerEntity getAvatar() {
        return this.getEntity();
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

    public PlayerSettings getSettings() {
        return this.settings;
    }

    public int getId() {
        return this.id;
    }
}
