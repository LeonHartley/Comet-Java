package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.RoomForwardMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.engine.UserNameChangeMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.UpdateUsernameMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.rooms.RoomDao;

import java.util.Map;


public class ChangeNameMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        String newName = msg.readString();
        String oldName = client.getPlayer().getData().getUsername();
        Map<Integer, IRoomData> rooms = RoomDao.getRoomsByPlayerId(client.getPlayer().getId());

        boolean inUse = false;

        if (client == null || client.getPlayer() == null || client.getPlayer().getData() == null || !client.getPlayer().getData().getChangingName()) {
            return;
        }

        if (newName.equals(oldName)) {
            if (client.getPlayer().getData().getFlaggingUser()) {
                client.send(new AdvancedAlertMessageComposer(Locale.getOrDefault("command.flaguser.alreadyexist.title", "Grrrr!"), Locale.getOrDefault("command.flaguser.alreadyexist", "Your name is inappropriate! That means that you're not allowed to choose your own inappropriate name again.")));
                return;
            } else {
                client.send(new UpdateUsernameMessageComposer(newName));
                client.getPlayer().getData().setChangingName(false);
                return;
            }
        }

        if (PlayerManager.getInstance().getPlayerIdByUsername(newName) != -1 || PlayerDao.getUsernameAlreadyExist(newName) != 0) {
            inUse = true;
            client.send(new AdvancedAlertMessageComposer(Locale.getOrDefault("command.flagme.alreadyexist.title", "Woops!"), Locale.getOrDefault("command.flagme.alreadyexist", "This name already exists! Try another name")));
        }

        char[] letters = newName.toLowerCase().toCharArray();
        String allowedCharacters = "abcdefghijklmnopqrstuvwxyz.,_-;:?!1234567890";

        for (char c : letters) {
            if (!allowedCharacters.contains(c + "")) {
                return;
            }
        }

        FilterResult filterResult = RoomManager.getInstance().getFilter().filter(newName);

        if (filterResult.isBlocked()) {
            return;
        }

        if (newName.toLowerCase().contains("mod") || newName.toLowerCase().contains("adm") || newName.toLowerCase().contains("admin") || newName.toLowerCase().contains("m0d") || newName.toLowerCase().contains("mob") || newName.toLowerCase().contains("m0b") || newName.length() > 15 || newName.length() < 3 || inUse) {
            return;
        }

        client.getPlayer().getData().setFlaggingUser(false);
        client.getPlayer().getData().setChangingName(false);

        if (client.getPlayer().getEntity() != null) {
            client.getPlayer().getEntity().kick();
            client.send(new UserNameChangeMessageComposer(client.getPlayer().getEntity().getRoom().getId(), client.getPlayer().getData().getId(), newName));
        } else {
            client.send(new UserNameChangeMessageComposer(-1, client.getPlayer().getData().getId(), newName));
        }

        client.getPlayer().getMessenger().setInitialised(true);
        client.send(new UpdateUsernameMessageComposer(newName));

        PlayerDao.updatePlayersUsername(newName, client.getPlayer().getId());
        PlayerDao.updateRoomsUsername(newName, client.getPlayer().getId());

        PlayerDao.saveNameChangeLog(client.getPlayer().getId(), newName, oldName);

        client.getPlayer().getData().setUsername(newName);
        PlayerManager.getInstance().updateUsernameCache(oldName, newName);

        for (Map.Entry<Integer, IRoomData> roomEntry : rooms.entrySet()) {
            IRoomData roomData = GameContext.getCurrent().getRoomService().getRoomData(roomEntry.getValue().getId());
            roomData.setOwner(newName);

            GameContext.getCurrent().getRoomService().saveRoomData(roomData);
        }

        client.getPlayer().getData().save();
        if (client.getPlayer().getEntity() != null) {
            client.getPlayer().getSession().send(new RoomForwardMessageComposer(client.getPlayer().getEntity().getRoom().getId()));
        }
    }
}
