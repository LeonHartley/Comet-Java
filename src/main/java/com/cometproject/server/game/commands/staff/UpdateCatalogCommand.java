package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.catalog.CatalogPublishMessageComposer;
import com.cometproject.server.network.sessions.Session;

/**
 * Created by Matty on 27/04/2014.
 */
public class UpdateCatalogCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        GameEngine.getCatalog().loadPages();
        GameEngine.getCatalog().loadClubOffers();

        Comet.getServer().getNetwork().getSessions().broadcast(CatalogPublishMessageComposer.compose(true));

        this.sendChat("Catalog has been reloaded", client);
    }

    @Override
    public String getPermission() {
        return "updatecatalog_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.updatecatalog.description");
    }
}
