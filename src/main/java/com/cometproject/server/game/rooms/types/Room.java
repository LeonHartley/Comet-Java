package com.cometproject.server.game.rooms.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.rooms.types.components.*;
import com.cometproject.server.game.rooms.types.mapping.RoomMapping;
import org.apache.log4j.Logger;

public class Room {
    private int id;
    private RoomData data;
    private RoomModel model;
    private RoomMapping mapping;

    private ProcessComponent process;
    private RightsComponent rights;
    private ItemsComponent items;
    private ItemProcessComponent itemProcess;
    private WiredComponent wired;
    private ChatlogComponent chatlog;
    private TradeComponent trade;
    private BotComponent bots;
    private GameComponent game;
    private EntityComponent entities;

    private Group group;

    public Logger log;
    public boolean isActive;

    public Room(RoomData data) {
        this.id = data.getId();
        this.data = data;
        this.model = GameEngine.getRooms().getModel(data.getModel());

        this.log = Logger.getLogger("Room \"" + this.getData().getName() + "\"");
        this.isActive = false;
        group = null; // TODO: this
    }

    public void load() {
        this.mapping = new RoomMapping(this, this.model);
        this.itemProcess = new ItemProcessComponent(Comet.getServer().getThreadManagement(), this);
        this.process = new ProcessComponent(this);
        this.rights = new RightsComponent(this);
        this.items = new ItemsComponent(this);
        this.wired = new WiredComponent(this);
        this.chatlog = new ChatlogComponent(this);
        this.trade = new TradeComponent(this);
        this.bots = new BotComponent(this);
        this.game = new GameComponent(this);
        this.entities = new EntityComponent(this, this.model);

        // Generate the mapping last
        this.mapping.init();

        this.isActive = true;
        this.log.debug("Room loaded");
    }

    public void dispose() {
        this.process.stop();
        this.itemProcess.stop();
        this.game.stop();

        this.itemProcess.dispose();
        this.process.dispose();
        this.rights.dispose();
        this.items.dispose();
        this.wired.dispose();
        this.chatlog.dispose();
        this.trade.dispose();
        this.bots.dispose();
        this.game.dispose();
        this.entities.dispose();

        this.itemProcess = null;
        this.process = null;
        this.rights = null;
        this.items = null;
        this.wired = null;
        this.chatlog = null;
        this.trade = null;
        this.bots = null;
        this.entities = null;

        this.isActive = false;
        this.log.debug("Room disposed");
    }

    public void tick() {
        if(bots != null)
            this.bots.tick();

        if(wired != null)
            this.wired.tick();
    }

    public ProcessComponent getProcess() {
        return this.process;
    }

    public ItemProcessComponent getItemProcess() {
        return this.itemProcess;
    }

    public ItemsComponent getItems() {
        return this.items;
    }

    public WiredComponent getWired() {
        return this.wired;
    }

    public ChatlogComponent getChatlog() {
        return this.chatlog;
    }

    public TradeComponent getTrade() {
        return this.trade;
    }

    public RightsComponent getRights() {
        return this.rights;
    }

    public BotComponent getBots() {
        return this.bots;
    }

    public GameComponent getGame() {
        return this.game;
    }

    public EntityComponent getEntities() {
        return this.entities;
    }

    public RoomMapping getMapping() {
        return this.mapping;
    }

    public int getId() {
        return this.id;
    }

    public RoomData getData() {
        return this.data;
    }

    public RoomModel getModel() {
        return this.model;
    }

    public Group getGroup() {
        return this.group;
    }

    // TODO: make component for misc functionality

    private int price = 0;
    private boolean forSale = false;

    public void setPrice(int i) {
        price = i;
    }

    public void setForSale(boolean f) {
        this.forSale = f;
    }

    public boolean isForSale() {
        return this.forSale;
    }

    public int getPrice() {
        return this.price;
    }
}
