package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.players.components.types.InventoryItem;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;

import java.util.Map;

public class EmptyCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        for (Map.Entry<Integer, InventoryItem> floorItem : client.getPlayer().getInventory().getFloorItems().entrySet()) {
            client.getPlayer().getInventory().removeFloorItem(floorItem.getKey());
        }

        for (Map.Entry<Integer, InventoryItem> wallItem : client.getPlayer().getInventory().getWallItems().entrySet()) {
            client.getPlayer().getInventory().removeWallItem(wallItem.getKey());
        }

        InventoryDao.clearInventory(client.getPlayer().getId());

        sendChat("Your inventory was cleared.", client);
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