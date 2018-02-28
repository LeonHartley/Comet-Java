package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.user.inventory.BotInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.PetInventoryMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.inventory.UpdateInventoryMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.bots.PlayerBotDao;
import com.cometproject.server.storage.queries.pets.PetDao;


public class EmptyPetsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        if (params.length != 1) {
            sendAlert(Locale.getOrDefault("command.emptypets.confirm", "<b>Warning!</b>\rAre you sure you want to delete all of your pets?\r\rIf you are sure type  <b>:" + Locale.get("command.emptypets.name") + " yes</b>"), client);
        } else {
            final String yes = Locale.getOrDefault("command.empty.yes", "yes");

            if (!params[0].equals(yes)) {
                sendAlert(Locale.getOrDefault("command.emptypets.confirm", "<b>Warning!</b>\rAre you sure you want to delete all of your pets?\r\rIf you are sure type  <b>:" + Locale.get("command.emptypets.name") + " yes</b>"), client);
            } else {
                PetDao.deletePets(client.getPlayer().getId());
                client.getPlayer().getPets().clearPets();

                client.send(new PetInventoryMessageComposer(client.getPlayer().getPets().getPets()));

                PlayerBotDao.deleteBots(client.getPlayer().getId());
                client.getPlayer().getBots().clearBots();
                client.send(new BotInventoryMessageComposer());

                sendNotif(Locale.getOrDefault("command.emptypets.emptied", "Your pets inventory was cleared."), client);
            }

            client.send(new UpdateInventoryMessageComposer());
        }
    }

    @Override
    public String getPermission() {
        return "emptypets_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.emptypets.description");
    }
}
