package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.details.NameChangeUpdateMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.util.LinkedList;


public class CheckValidNameMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        String name = msg.readString();

        boolean inUse = false;

        if (client == null || client.getPlayer() == null || client.getPlayer().getData() == null || !client.getPlayer().getData().getChangingName()) {
            return;
        }

        if (PlayerManager.getInstance().getPlayerIdByUsername(name) != -1 || PlayerDao.getUsernameAlreadyExist(name) != 0) {
            inUse = true;
        }

        char[] letters = name.toLowerCase().toCharArray();
        String allowedCharacters = "abcdefghijklmnopqrstuvwxyz.,_-;:?!1234567890";

        for (char chr : letters) {
            if (!allowedCharacters.contains(chr + "")) {
                client.send(new NameChangeUpdateMessageComposer(name, 4));
                return;
            }
        }

        FilterResult filterResult = RoomManager.getInstance().getFilter().filter(name);

        if (filterResult.isBlocked()) {
            filterResult.sendLogToStaffs(client, "<ChangingName>");
            client.send(new NameChangeUpdateMessageComposer(name, 4));
            return;
        }

        if (name.toLowerCase().contains("mod") || name.toLowerCase().contains("adm") || name.toLowerCase().contains("admin") || name.toLowerCase().contains("m0d") || name.toLowerCase().contains("mob") || name.toLowerCase().contains("m0b")) {
            client.send(new NameChangeUpdateMessageComposer(name, 4));
            return;
        } else if (name.length() > 15) {
            client.send(new NameChangeUpdateMessageComposer(name, 3));
            return;
        } else if (name.length() < 3) {
            client.send(new NameChangeUpdateMessageComposer(name, 2));
            return;
        } else if (inUse) {
            LinkedList<String> suggestions = new LinkedList();
            for (int i = 100; i < 103; i++) {
                suggestions.add(i + "");
            }
            client.send(new NameChangeUpdateMessageComposer(name, 5, suggestions));
            return;
        } else {
            client.send(new NameChangeUpdateMessageComposer(name, 0));
            return;
        }
    }
}
