package com.cometproject.server.game.commands.user;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.messenger.UpdateFriendStateMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;
import com.cometproject.server.storage.queries.player.relationships.RelationshipDao;

public class EmptyFriendsCommand extends ChatCommand {
    @Override
    public void execute(Session client, String[] params) {
        MessengerDao.deleteAllFriendships(client.getPlayer().getId());

        for (Integer playerId : client.getPlayer().getMessenger().getFriends().keySet()) {
            Session removedUser = NetworkManager.getInstance().getSessions().getByPlayerId(playerId);

            if (removedUser == null)
                continue;


            if (client.getPlayer().getRelationships().getRelationships().containsKey(playerId)) {
                client.send(new UpdateFriendStateMessageComposer(removedUser.getPlayer().getData(),
                        removedUser.getPlayer().isOnline(), removedUser.getPlayer().getEntity() != null,
                        removedUser.getPlayer().getRelationships().get(removedUser.getPlayer().getData().getId())));

                if (removedUser.getPlayer().getRelationships().getRelationships().containsKey(client.getPlayer().getData().getId())) {
                    removedUser.send(new UpdateFriendStateMessageComposer(client.getPlayer().getData(),
                            client.getPlayer().isOnline(), client.getPlayer().getEntity() != null, client.getPlayer().getRelationships().get(client.getPlayer().getData().getId())));
                }
            }

            removedUser.getPlayer().getRelationships().getRelationships().remove(client.getPlayer().getData().getId());
            RelationshipDao.deleteRelationship(removedUser.getPlayer().getData().getId(), client.getPlayer().getData().getId());


            client.send(new UpdateFriendStateMessageComposer(-1, removedUser.getPlayer().getData().getId()));
            removedUser.getPlayer().getMessenger().getFriends().remove(client.getPlayer().getData().getId());
            removedUser.send(new UpdateFriendStateMessageComposer(-1, client.getPlayer().getData().getId()));

            removedUser.flush();
        }

        client.getPlayer().getRelationships().getRelationships().clear();
        client.getPlayer().getMessenger().getFriends().clear();
        RelationshipDao.emptyRelationship(client.getPlayer().getData().getId());

        client.flush();

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
