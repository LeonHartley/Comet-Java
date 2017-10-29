package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.relationships.RelationshipDao;

public class EmptyRelationshipsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        RelationshipDao.emptyRelationship(client.getPlayer().getId());
        client.send(new AdvancedAlertMessageComposer(Locale.getOrDefault("command.emptyrelationships.sucess", "Success!")));
    }

    @Override
    public String getPermission() {
        return "emptyrelationships_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.emptyrelationships.description");
    }
}
