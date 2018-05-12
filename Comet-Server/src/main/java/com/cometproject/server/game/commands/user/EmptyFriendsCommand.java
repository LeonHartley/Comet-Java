package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.messenger.UpdateFriendStateMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;

public class EmptyFriendsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        MessengerDao.deleteAllFriendships(client.getPlayer().getId());

        for(Integer playerId : client.getPlayer().getMessenger().getFriends().keySet()) {
            client.getPlayer().getSession().send(new UpdateFriendStateMessageComposer(-1, playerId));
        }

        client.getPlayer().getMessenger().getFriends().clear();
        client.getPlayer().getMessenger().initialise();

        sendAlert(Locale.getOrDefault("command.emptyfriends.success", "All friendships deleted successfully"), client);
    }

    @Override
    public String getPermission() {
        return "emptyfriends_command";
    }

    @Override
    public String getParameter() {
        return "";
    }

    @Override
    public String getDescription() {
        return Locale.getOrDefault("command.emptyfriends.description", "Deletes all of your friendships");
    }
}
