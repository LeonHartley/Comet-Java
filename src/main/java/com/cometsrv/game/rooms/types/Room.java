package com.cometsrv.game.rooms.types;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.GameEngine;
import com.cometsrv.game.groups.types.Group;
import com.cometsrv.game.rooms.types.components.*;
import org.apache.log4j.Logger;

public class Room {
    private int id;
    private RoomData data;
    private RoomModel model;

    private AvatarComponent avatars;
    private ProcessComponent process;
    private RightsComponent rights;
    private ItemsComponent items;
    private ItemProcessComponent itemProcess;
    private WiredComponent wired;
    private ChatlogComponent chatlog;
    private TradeComponent trade;
    private BotComponent bots;
    private GameComponent game;

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
        this.itemProcess = new ItemProcessComponent(Comet.getServer().getThreadManagement(), this);
        this.avatars = new AvatarComponent(this);
        this.process = new ProcessComponent(this);
        this.rights = new RightsComponent(this);
        this.items = new ItemsComponent(this);
        this.wired = new WiredComponent(this);
        this.chatlog = new ChatlogComponent(this);
        this.trade = new TradeComponent(this);
        this.bots = new BotComponent(this);
        this.game = new GameComponent(this);

        this.isActive = true;
        this.log.debug("Room loaded");
    }

    public void dispose() {
        this.process.stop();
        this.itemProcess.stop();
        this.game.stop();

        this.itemProcess.dispose();
        this.avatars.dispose();
        this.process.dispose();
        this.rights.dispose();
        this.items.dispose();
        this.wired.dispose();
        this.chatlog.dispose();
        this.trade.dispose();
        this.bots.dispose();
        this.game.dispose();

        this.itemProcess = null;
        this.avatars = null;
        this.process = null;
        this.rights = null;
        this.items = null;
        this.wired = null;
        this.chatlog = null;
        this.trade = null;
        this.bots = null;

        this.isActive = false;
        this.log.debug("Room disposed");
    }

    public void tick() {
        if(bots != null)
            this.bots.tick();
    }

    public AvatarComponent getAvatars() {
        return this.avatars;
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
