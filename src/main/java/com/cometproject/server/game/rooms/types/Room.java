package com.cometproject.server.game.rooms.types;

import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.models.CustomFloorMapData;
import com.cometproject.server.game.rooms.models.RoomModel;
import com.cometproject.server.game.rooms.models.types.DynamicRoomModel;
import com.cometproject.server.game.rooms.types.components.*;
import com.cometproject.server.game.rooms.types.mapping.RoomMapping;
import com.cometproject.server.tasks.CometThreadManager;
import com.cometproject.server.utilities.JsonFactory;
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
        this.model = RoomManager.getInstance().getModel(this.getData().getModel());

        if (this.getData().getHeightmap() != null) {
            DynamicRoomModel dynamicRoomModel;

            if (this.getData().getHeightmap().startsWith("{")) {
                CustomFloorMapData mapData = JsonFactory.getInstance().fromJson(this.getData().getHeightmap(), CustomFloorMapData.class);

                dynamicRoomModel = DynamicRoomModel.create("dynamic_heightmap", mapData.getModelData(), mapData.getDoorX(), mapData.getDoorY(), this.getModel().getDoorZ(), mapData.getDoorRotation(), mapData.getWallHeight());
            } else {
                dynamicRoomModel = DynamicRoomModel.create("dynamic_heightmap", this.getData().getHeightmap(), this.getModel().getDoorX(), this.getModel().getDoorY(), this.getModel().getDoorZ(), this.getModel().getDoorRotation(), -1);
            }

            if (dynamicRoomModel != null) {
                this.model = dynamicRoomModel;
            }

        }

        this.attributes = new FastMap<>();

        this.mapping = new RoomMapping(this);
        this.itemProcess = new ItemProcessComponent(CometThreadManager.getInstance(), this);
        this.process = new ProcessComponent(this);
        this.rights = new RightsComponent(this);
        this.items = new ItemsComponent(this);
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
        if (this.idleTicks < 600 && this.getEntities().realPlayerCount() > 0) {
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
        if (this.isDisposed) {
            return;
        }
        this.isDisposed = true;

        this.process.stop();
        this.itemProcess.stop();
        this.game.stop();

        this.game.dispose();
        this.entities.dispose();
        this.items.dispose();

        this.log.debug("Room has been disposed");
    }

    public void tick() {
        if (this.getPromotion() != null) {
            if (this.getPromotion().isExpired()) {
                // The room isn't promoted anymore!
                RoomManager.getInstance().getRoomPromotions().remove(this.getId());

                // Remove the event from the room!
            }
        }

        if (useCycleForEntities && this.process != null) {
            this.process.tick();
        }

        if (useCycleForItems && this.itemProcess != null) {
            this.itemProcess.tick();
        }

        if (this.rights != null) {
            this.rights.tick();
        }
    }

    public RoomPromotion getPromotion() {
        return RoomManager.getInstance().getRoomPromotions().get(this.getId());
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
        return this.attributes.containsKey("room_muted") && (boolean) this.attributes.get("room_muted");
    }

    public void setRoomMute(boolean mute) {
        if (this.attributes.containsKey("room_muted")) {
            this.attributes.replace("room_muted", mute);
        } else {
            this.attributes.put("room_muted", mute);
        }
    }
}
