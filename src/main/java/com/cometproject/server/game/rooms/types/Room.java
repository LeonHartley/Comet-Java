package com.cometproject.server.game.rooms.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.models.RoomModel;
import com.cometproject.server.game.rooms.models.types.DynamicRoomModel;
import com.cometproject.server.game.rooms.models.types.StaticRoomModel;
import com.cometproject.server.game.rooms.types.components.*;
import com.cometproject.server.game.rooms.types.mapping.RoomMapping;
import com.cometproject.server.utilities.attributes.Attributable;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.*;

public class Room implements Attributable {
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
    private PetComponent pets;
    private GameComponent game;
    private EntityComponent entities;

    public Logger log;
    public boolean isActive;
    private boolean isRoomMuted = false;

    private Map<String, Object> attributes;

    public Room(RoomData data) {
        this.id = data.getId();
        this.data = data;

        this.model = CometManager.getRooms().getModel(data.getModel());

        this.log = Logger.getLogger("Room \"" + this.getData().getName() + "\"");
        this.isActive = false;
    }

    public void load() {
        if(data.getHeightmap() != null && this.model instanceof StaticRoomModel) {
            this.model = new DynamicRoomModel("dynamic_heightmap", data.getHeightmap(), this.model.getDoorX(), this.model.getDoorY(), this.model.getDoorZ(), this.model.getDoorRotation());
        }

        this.attributes = new FastMap<>();

        this.mapping = new RoomMapping(this, this.model);
        this.itemProcess = new ItemProcessComponent(Comet.getServer().getThreadManagement(), this);
        this.process = new ProcessComponent(this);
        this.rights = new RightsComponent(this);
        this.items = new ItemsComponent(this);
        this.wired = new WiredComponent(this);
        this.chatlog = new ChatlogComponent(this);
        this.trade = new TradeComponent(this);
        this.game = new GameComponent(this);
        this.entities = new EntityComponent(this, this.model);
        this.bots = new BotComponent(this);
        this.pets = new PetComponent(this);

        // LOAD ALL LE ITEMZ!!!!!!
        this.items.callOnLoad();

        // Generate the mapping last
        this.mapping.init();

        this.isActive = true;

        CometManager.getRooms().getActiveRoomIds().add(this.id);

        this.log.debug("Room loaded");
    }

    public void dispose() {
        this.data.save();

        this.process.stop();
        this.itemProcess.stop();
        this.game.stop();

        this.itemProcess.dispose();
        this.process.dispose();
        this.entities.dispose();
        this.rights.dispose();
        this.items.dispose();
        this.wired.dispose();
        this.chatlog.dispose();
        this.trade.dispose();
        this.bots.dispose();
        this.pets.dispose();
        this.game.dispose();
        this.mapping.dispose();

        for(Map.Entry<String, Object> attribute : this.attributes.entrySet()) {
            if(attribute.getValue() instanceof Collection) {
                ((Collection) attribute.getValue()).clear();
            }
        }

        this.attributes.clear();

        this.attributes = null;
        this.itemProcess = null;
        this.process = null;
        this.rights = null;
        this.items = null;
        this.wired = null;
        this.chatlog = null;
        this.trade = null;
        this.bots = null;
        this.pets = null;
        this.entities = null;
        this.mapping = null;

        if(this.model instanceof DynamicRoomModel) {
            this.model.dispose();

            this.model = CometManager.getRooms().getModel(this.data.getModel());
        }

        this.isActive = false;

        CometManager.getRooms().getActiveRoomIds().remove(this.id);

        this.log.debug("Room disposed");
    }

    public void tick() {
        if (wired != null)
            this.wired.tick();
    }

    @Override
    public void setAttribute(String attributeKey, Object attributeValue) {
        if (this.attributes.containsKey(attributeKey)) {
            this.removeAttribute(attributeKey);
        }

        this.attributes.put(attributeKey, attributeValue);
    }

    @Override
    public Object getAttribute(String attributeKey) {
        return this.attributes.get(attributeKey);
    }

    @Override
    public boolean hasAttribute(String attributeKey) {
        return this.attributes.containsKey(attributeKey);
    }

    @Override
    public void removeAttribute(String attributeKey) {
        this.attributes.remove(attributeKey);
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

    public PetComponent getPets() {
        return this.pets;
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

    // TODO: remove below to attributes
    public boolean hasRoomMute() {
        return this.isRoomMuted;
    }

    public void setRoomMute(boolean mute) {
        this.isRoomMuted = mute;
    }
}
