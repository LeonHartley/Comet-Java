package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.user.inventory.BotInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.bots.PlayerBotDao;
import com.cometproject.server.storage.queries.pets.PetDao;
import com.cometproject.server.storage.queries.player.inventory.InventoryDao;


public class EmptyCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendAlert(Locale.getOrDefault("command.empty.confirm", "<b>Warning!</b>\rAre you sure? You are going to delete your Furni, Bots & Pets.\r\rIf you are sure type  <b>:" + Locale.get("command.empty.name")  + " yes</b>"), client);
        } else {
            switch (params[0]) {
                default:
                    sendAlert(Locale.getOrDefault("command.empty.confirm", "<b>Warning!</b>\rAre you sure? You are going to delete your Furni, Bots & Pets.\r\rIf you are sure type  <b>:" + Locale.get("command.empty.name")  + " yes</b>"), client);
                    break;

                case "yes":
                    client.getPlayer().getInventory().getFloorItems().clear();
                    client.getPlayer().getInventory().getWallItems().clear();

                    InventoryDao.clearInventory(client.getPlayer().getId());

                    PetDao.deletePets(client.getPlayer().getId());
                    client.getPlayer().getPets().clearPets();

                    client.send(new PetInventoryMessageComposer(client.getPlayer().getPets().getPets()));

                    PlayerBotDao.deleteBots(client.getPlayer().getId());
                    client.getPlayer().getBots().clearBots();
                    client.send(new BotInventoryMessageComposer());

                    sendNotif(Locale.getOrDefault("command.empty.emptied", "Your inventory was cleared."), client);
            }
            
            client.send(new UpdateInventoryMessageComposer());
        }
    }

    @Override
    public String getPermission() {
        return "empty_command";
    }
    
    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.empty.description");
    }
}
