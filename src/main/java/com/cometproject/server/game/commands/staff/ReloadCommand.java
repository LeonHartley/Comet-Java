package com.cometproject.server.game.commands.staff;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.catalog.CataIndexMessageComposer;
import com.cometproject.server.network.messages.outgoing.catalog.CatalogPublishMessageComposer;
import com.cometproject.server.network.messages.outgoing.misc.MotdNotificationComposer;
import com.cometproject.server.network.sessions.Session;

public class ReloadCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        if (params.length < 1)
            return;

        String command = params[0];

        switch (command) {
            case "list":
                client.send(MotdNotificationComposer.compose(
                        "- bans\n" +
                                "- catalog\n" +
                                "- navigator\n" +
                                "- permissions\n" +
                                "- catalog\n" +
                                "- news\n" +
                                "- config\n" +
                                "- items\n" +
                                "- filter\n"
                ));

                break;
            case "bans":
                CometManager.getBans().loadBans();

                sendChat(Locale.get("command.reload.bans"), client);
                break;

            case "catalog":
                CometManager.getCatalog().loadPages();

                Comet.getServer().getNetwork().getSessions().broadcast(CatalogPublishMessageComposer.compose(true));
                sendChat(Locale.get("command.reload.catalog"), client);
                break;

            case "navigator":
                CometManager.getNavigator().loadFeaturedRooms();
                sendChat(Locale.get("command.reload.navigator"), client);
                break;

            case "permissions":
                CometManager.getPermissions().loadPermissions();
                CometManager.getPermissions().loadPerks();
                CometManager.getPermissions().loadCommands();

                sendChat(Locale.get("command.reload.permissions"), client);
                break;

            case "config":
                // TODO: Finish config rewrite.. ;p
//                Comet.getServer().loadConfig();
                sendChat(Locale.get("command.reload.config"), client);
                break;

            case "news":
                CometManager.getLanding().loadArticles();

                sendChat(Locale.get("command.reload.news"), client);
                break;

            case "items":
                CometManager.getItems().loadItemDefinitions();

                sendChat(Locale.get("command.reload.items"), client);
                break;

            case "filter":
                CometManager.getRooms().getFilter().loadFilter();

                sendChat(Locale.get("command.reload.filter"), client);
                break;
        }
    }

    @Override
    public String getPermission() {
        return "reload_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.reload.description");
    }
}
