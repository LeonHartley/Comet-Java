package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.components.types.inventory.InventoryItem;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.bots.PlayerBotDao;
import com.cometproject.server.storage.queries.pets.PetDao;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;

import java.util.Map;


public class EmptyCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if(params.length != 1) {
            for (Map.Entry<Integer, InventoryItem> floorItem : client.getPlayer().getInventory().getFloorItems().entrySet()) {
                client.getPlayer().getInventory().removeFloorItem(floorItem.getKey());
            }

            for (Map.Entry<Integer, InventoryItem> wallItem : client.getPlayer().getInventory().getWallItems().entrySet()) {
                client.getPlayer().getInventory().removeWallItem(wallItem.getKey());
            }

            InventoryDao.clearInventory(client.getPlayer().getId());
            sendNotif(Locale.getOrDefault("command.empty.emptied", "Your inventory was cleared."), client);
        } else {
            switch(params[0]) {
                default:

                    break;

                case "pets":
                    PetDao.deletePets(client.getPlayer().getId());
                    client.getPlayer().getPets().clearPets();

                    sendNotif(Locale.getOrDefault("command.empty.emptied_pets", "Your inventory was cleared."), client);
                    return;

                case "bots":
                    PlayerBotDao.deleteBots(client.getPlayer().getId());
                    client.getPlayer().getBots().clearBots();

                    sendNotif(Locale.getOrDefault("command.empty.emptied_bots", "Your inventory was cleared."), client);
                    return;
            }
        }

        client.send(UpdateInventoryMessageComposer.compose());
    }

    @Override
    public String getPermission() {
        return "empty_command";
    }

    @Override
    public String getDescription() {

        return Locale.get("command.empty.description");
    }
}