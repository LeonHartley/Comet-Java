package com.cometproject.server.game.rooms.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.models.RoomModel;
import com.cometproject.server.game.rooms.models.types.DynamicRoomModel;
import com.cometproject.server.game.rooms.types.components.*;
import com.cometproject.server.game.rooms.types.mapping.RoomMapping;
import com.cometproject.server.utilities.attributes.Attributable;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.Map;

public class Room implements Attributable {
    public static final boolean useCycleForItems = false;
    public static final boolean useCycleForEntities = false;

    public Logger log;

    private RoomData data;
    private RoomModel model;
    private RoomMapping mapping;

    private ProcessComponent process;
    private RightsComponent rights;
    private ItemsComponent items;
    private ItemProcessComponent itemProcess;
    private WiredComponent wired;
    private TradeComponent trade;
    private BotComponent bots;
    private PetComponent pets;
    private GameComponent game;
    private EntityComponent entities;

    private Map<String, Object> attributes;

    private boolean isDisposed = false;
    private int idleTicks = 0;

    public Room(RoomData data) {
        this.data = data;
        this.log = Logger.getLogger("Room \"" + this.getData().getName() + "\"");
    }

    public Room load() {
        this.model = CometManager.getRooms().getModel(this.getData().getModel());

        if (this.getData().getHeightmap() != null) {
            this.model = new DynamicRoomModel("dynamic_heightmap", this.getData().getHeightmap(), this.getModel().getDoorX(), this.getModel().getDoorY(), this.getModel().getDoorZ(), this.getModel().getDoorRotation());
        }

        this.attributes = new FastMap<>();

        this.mapping = new RoomMapping(this);
        this.itemProcess = new ItemProcessComponent(Comet.getServer().getThreadManagement(), this);
        this.process = new ProcessComponent(this);
        this.rights = new RightsComponent(this);
        this.items = new ItemsComponent(this);
        this.wired = new WiredComponent(this);
        this.trade = new TradeComponent(this);
        this.game = new GameComponent(this);
        this.entities = new EntityComponent(this);
        this.bots = new BotComponent(this);
        this.pets = new PetComponent(this);

        this.mapping.init();

        this.setAttribute("loadTime", System.currentTimeMillis());

        this.log.debug("Room loaded");
        return this;
    }

    public boolean isIdle() {
        if (this.idleTicks < 600 && this.getEntities().playerCount() > 0) {
            this.idleTicks = 0;
        } else {
            if (this.idleTicks >= 600) {
                return true;
            } else {
                this.idleTicks += 10;
            }
        }

        return false;
    }

    public void setIdleNow() {
        this.idleTicks = 600;
    }

    public void dispose() {
        if (this.isDisposed) { return; }
        this.isDisposed = true;

        this.process.stop();
        this.itemProcess.stop();
        this.game.stop();

        this.entities.dispose();
        this.items.dispose();

        this.log.debug("Room has been disposed");
    }

    public boolean hasGroup() {
        return CometManager.getGroups().get(this.getId()) == null;
    }

    public void tick() {
        this.wired.tick();

        if (useCycleForEntities && this.process != null) {
            this.process.tick();
        }

        if (useCycleForItems && this.itemProcess != null) {
            this.itemProcess.tick();
        }
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

    public int getId() {
        return this.data.getId();
    }

    public RoomData getData() {
        return this.data;
    }

    public RoomModel getModel() {
        return this.model;
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

    public boolean hasRoomMute() {
        return this.attributes.containsKey("room_muted") && (boolean)this.attributes.get("room_muted");
    }

    public void setRoomMute(boolean mute) {
        if (this.attributes.containsKey("room_muted")) {
            this.attributes.replace("room_muted", mute);
        } else {
            this.attributes.put("room_muted", mute);
        }
    }
}
