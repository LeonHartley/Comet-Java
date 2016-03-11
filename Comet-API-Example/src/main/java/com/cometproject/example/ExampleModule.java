package com.cometproject.example;

import com.cometproject.api.config.ModuleConfig;
import com.cometproject.api.events.players.OnPlayerLoginEvent;
import com.cometproject.api.events.players.args.OnPlayerLoginEventArgs;
import com.cometproject.api.game.players.data.components.inventory.PlayerItem;
import com.cometproject.api.modules.BaseModule;
import com.cometproject.api.networking.sessions.BaseSession;
import com.cometproject.api.server.IGameService;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ExampleModule extends BaseModule {
    public ExampleModule(ModuleConfig config, IGameService gameService) {
        super(config, gameService);

        this.registerEvent(new OnPlayerLoginEvent(this::onPlayerLogin));

        this.registerChatCommand("!about", this::aboutCommand);
        this.registerChatCommand("!inventory", this::inventoryCommand);

        this.registerChatCommand("!mathis", this::mathisCommand);
    }

    public void mathisCommand(BaseSession session, String[] args) {
        session.getPlayer().sendMotd("Hi mathis, how are you????!!!!!");
    }

    public void aboutCommand(BaseSession session, String[] args) {
        session.getPlayer().sendNotif("ExampleModule", "This is an example module.");
    }

    public void inventoryCommand(BaseSession session, String[] args) {
        if (!session.getPlayer().getInventory().itemsLoaded()) {
            session.getPlayer().getInventory().loadItems();
        }

        StringBuilder inventoryStr = new StringBuilder("Inventory:\n===================================================\n\n");

        Map<String, AtomicInteger> inventoryItemsAndQuantity = Maps.newHashMap();

        for (PlayerItem item : session.getPlayer().getInventory().getFloorItems().values()) {
            if (inventoryItemsAndQuantity.containsKey(item.getDefinition().getPublicName()))
                inventoryItemsAndQuantity.get(item.getDefinition().getPublicName()).incrementAndGet();
            else
                inventoryItemsAndQuantity.put(item.getDefinition().getPublicName(), new AtomicInteger(1));
        }

        for (PlayerItem item : session.getPlayer().getInventory().getWallItems().values()) {
            if (inventoryItemsAndQuantity.containsKey(item.getDefinition().getPublicName()))
                inventoryItemsAndQuantity.get(item.getDefinition().getPublicName()).incrementAndGet();
            else
                inventoryItemsAndQuantity.put(item.getDefinition().getPublicName(), new AtomicInteger(1));
        }

        for (Map.Entry<String, AtomicInteger> item : inventoryItemsAndQuantity.entrySet()) {
            inventoryStr.append(item.getValue().get() + " x " + item.getKey() + "\n");
        }

        session.getPlayer().sendMotd(inventoryStr.toString());
    }

    public void onPlayerLogin(OnPlayerLoginEventArgs eventArgs) {
//         player = eventArgs.getPlayer();
//
//        player.sendNotif("Welcome!", "Hey " + eventArgs.getPlayer().getData().getUsername() + ", you've received your login bonus!");
//
//        player.getData().increaseCredits(100);
//        player.getData().save();
//
//        player.sendBalance();
    }
}
