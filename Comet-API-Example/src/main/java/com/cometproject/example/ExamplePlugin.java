package com.cometproject.example;

import com.cometproject.api.config.ModuleConfig;
import com.cometproject.api.events.players.OnPlayerLoginEvent;
import com.cometproject.api.events.players.args.OnPlayerLoginEventArgs;
import com.cometproject.api.game.players.IPlayer;
import com.cometproject.api.game.players.data.components.inventory.IInventoryItem;
import com.cometproject.api.modules.CometModule;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.api.server.IGameService;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ExamplePlugin extends CometModule {
    public ExamplePlugin(ModuleConfig config, IGameService gameService) {
        super(config, gameService);

        this.registerEvent(new OnPlayerLoginEvent(this::onPlayerLogin));

        // register commands
        this.registerChatCommand("!about", this::aboutCommand);
        this.registerChatCommand("!inventory", this::inventoryCommand);
    }

    public void aboutCommand(ISession session, String[] args) {
        session.getPlayer().sendNotif("ExamplePlugin", "This is an example plugin.");
    }

    public void inventoryCommand(ISession session, String[] args) {
        if(!session.getPlayer().getInventory().itemsLoaded()) {
            session.getPlayer().getInventory().loadItems();
        }

        StringBuilder inventoryStr = new StringBuilder("Inventory items:<br><br>");

        Map<String, AtomicInteger> inventoryItemsAndQuantity = Maps.newHashMap();

        for(IInventoryItem item : session.getPlayer().getInventory().getFloorItems().values()) {
            if(inventoryItemsAndQuantity.containsKey(item.getDefinition().getPublicName()))
                inventoryItemsAndQuantity.get(item.getDefinition().getPublicName()).incrementAndGet();
            else
                inventoryItemsAndQuantity.put(item.getDefinition().getPublicName(), new AtomicInteger(1));
        }

        for(IInventoryItem item : session.getPlayer().getInventory().getWallItems().values()) {
            if(inventoryItemsAndQuantity.containsKey(item.getDefinition().getPublicName()))
                inventoryItemsAndQuantity.get(item.getDefinition().getPublicName()).incrementAndGet();
            else
                inventoryItemsAndQuantity.put(item.getDefinition().getPublicName(), new AtomicInteger(1));
        }

        for(Map.Entry<String, AtomicInteger> item : inventoryItemsAndQuantity.entrySet()) {
            inventoryStr.append(item.getValue().get() + " x " + item.getKey() + "<br>");
        }

        session.getPlayer().sendNotif("Inventory", inventoryStr.toString());
    }

    public void onPlayerLogin(OnPlayerLoginEventArgs eventArgs) {
        IPlayer player = eventArgs.getPlayer();

        player.sendNotif("Welcome!", "Hey " + eventArgs.getPlayer().getData().getUsername() + ", you've received your login bonus!");

        player.getData().increaseCredits(100);
        player.getData().save();

        player.sendBalance();
    }
}
