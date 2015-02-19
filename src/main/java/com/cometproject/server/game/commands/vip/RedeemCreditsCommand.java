package com.cometproject.server.game.commands.vip;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.google.common.collect.Lists;

import java.util.List;


public class RedeemCreditsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        int coinsToGive = 0;
        List<Integer> itemsToRemove = Lists.newArrayList();

        if(!client.getPlayer().getInventory().itemsLoaded()) {
            sendNotif(Locale.getOrDefault("command.redeemcredits.inventory", "Please open your inventory before executing this command!"), client);
            return;
        }

        for (InventoryItem inventoryItem : client.getPlayer().getInventory().getFloorItems().values()) {
            if (inventoryItem == null || inventoryItem.getDefinition() == null) continue;

            String itemName = inventoryItem.getDefinition().getItemName();

            if (itemName.startsWith("CF_") || itemName.startsWith("CFC_")) {
                try {
                    int coinage = Integer.parseInt(itemName.split("_")[1]);

                    itemsToRemove.add(inventoryItem.getId());

                    coinsToGive += coinage;
                    RoomItemDao.deleteItem(inventoryItem.getId());
                } catch (Exception ignored) {

                }
            }
        }

        if (itemsToRemove.size() == 0) {
            return;
        }

        for (int itemId : itemsToRemove) {
            client.getPlayer().getInventory().removeFloorItem(itemId);
        }

        itemsToRemove.clear();

        client.send(UpdateInventoryMessageComposer.compose());
        client.getPlayer().getData().increaseCredits(coinsToGive);
        client.getPlayer().sendBalance();

        client.getPlayer().getData().save();
    }


    @Override
    public String getPermission() {
        return "redeemcredits_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.redeemcredits.description");
    }
}
