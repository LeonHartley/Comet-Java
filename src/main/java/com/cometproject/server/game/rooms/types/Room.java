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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Room implements Attributable {
    public Logger log;

    private int id;

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

    private boolean isRoomMuted = false;
    private boolean isDisposed = false;

    public static boolean useCycleForItems = false;
    public static boolean useCycleForEntities = false;

    private AtomicBoolean needsRemoving = new AtomicBoolean(false);
    private AtomicInteger idleTicks = new AtomicInteger(0);

    private Map<String, Object> attributes;

    public Room(RoomData data) {
        this.id = data.getId();

        this.log = Logger.getLogger("Room \"" + this.getData().getName() + "\"");

        // Now we auto load the room data instead of calling it manually!
        this.load();
    }

    public boolean needsRemoving() {
        return this.needsRemoving.get();
    }

    public void setNeedsRemoving() {
        this.needsRemoving.set(true);
    }

    public void unIdleIfRequired() {
        if (this.entities.playerCount() > 0) {
            if (this.idleTicks.get() > 0) {
                this.idleTicks.set(0);
            }

            if (this.needsRemoving.get()) {
                this.needsRemoving.set(false);
            }
        }
    }

    public boolean isIdle() {
        if (this.needsRemoving.get()) { return true; }

        if (this.idleTicks.get() < 10 && this.entities.reliablePlayerCountTest() == 0) {
            this.idleTicks.incrementAndGet();
        } else if (this.idleTicks.get() > 0 && this.entities.reliablePlayerCountTest() > 0) {
            this.idleTicks.set(0);
        } else if (this.idleTicks.get() >= 10) {
            this.needsRemoving.set(true);
            return true;
        }

        return false;
    }

    private void load() {
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

        // LOAD ALL LE ITEMZ!!!!!!
        this.items.callOnLoad();

        // Generate the mapping last
        this.mapping.init();

        this.log.debug("Room loaded");
    }

    public void dispose() {
        if (this.isDisposed) {
            return;
        }

        this.isDisposed = true;

        this.getData().save();

        this.process.stop();
        this.itemProcess.stop();
        this.game.stop();

        this.itemProcess.dispose();
        this.process.dispose();
        this.entities.dispose();
        this.rights.dispose();
        this.items.dispose();
        this.wired.dispose();
        this.trade.dispose();
        this.bots.dispose();
        this.pets.dispose();
        this.game.dispose();
        this.mapping.dispose();

        this.attributes.clear();

        if (this.model instanceof DynamicRoomModel) {
            this.model.dispose();
        }

        this.log.debug("Room disposed");
    }

    public void tick() {
            this.wired.tick();

        if(useCycleForEntities && this.process != null)
            this.process.tick();

        if(useCycleForItems && this.itemProcess != null)
            this.itemProcess.tick();
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
        return CometManager.getRooms().getRoomData(this.id);
    }

    public RoomModel getModel() {
        if(this.model == null)
            return CometManager.getRooms().getModel(this.getData().getModel());

        return this.model;
    }

    // TODO: remove below to attributes
    public boolean hasRoomMute() {
        return this.isRoomMuted;
    }

    public void setRoomMute(boolean mute) {
        this.isRoomMuted = mute;
    }

    public boolean isDisposed() {
        return isDisposed;
    }
}
