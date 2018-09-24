package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.entries.RoomChatLogEntry;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.MutedMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class WhisperMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        String text = msg.readString();

        String user = text.split(" ")[0];
        String message = text.substring(user.length() + 1);

        final int timeMutedExpire = client.getPlayer().getData().getTimeMuted() - (int) Comet.getTime();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        if (!client.getPlayer().getEntity().isVisible()) {
            return;
        }

        if (client.getPlayer().getData().getTimeMuted() != 0) {
            if (client.getPlayer().getData().getTimeMuted() > (int) Comet.getTime()) {
                client.getPlayer().getSession().send(new MutedMessageComposer(timeMutedExpire));
                return;
            }
        }

        final Room room = client.getPlayer().getEntity().getRoom();

        PlayerEntity userTo = room.getEntities().getPlayerEntityByName(user);

        if (userTo == null || user.equals(client.getPlayer().getData().getUsername()))
            return;

        if (!userTo.getPlayer().getEntity().isVisible())
            return;

        if(userTo.getPlayer().getSettings().disableWhisper() && !(client.getPlayer().getData().getRank() > Integer.parseInt(Locale.getOrDefault("whisper.minrank.force", "5")))) {
            client.send(new WhisperMessageComposer(client.getPlayer().getId(), Locale.getOrDefault("whisper.disabled", "Oops!, the user who you're trying to whisper has whispers disabled!")));
            return;
        }

        if (client.getPlayer().getChatMessageColour() != null) {
            message = "@" + client.getPlayer().getChatMessageColour() + "@" + message;

            if (message.toLowerCase().startsWith("@" + client.getPlayer().getChatMessageColour() + "@:")) {
                message = message.toLowerCase().replace("@" + client.getPlayer().getChatMessageColour() + "@:", ":");
            }
        }

        final PlayerEntity playerEntity = client.getPlayer().getEntity();

        String filteredMessage = TalkMessageEvent.filterMessage(message);

        if (filteredMessage == null) {
            return;
        }

        if (!client.getPlayer().getPermissions().getRank().roomFilterBypass()) {
            FilterResult filterResult = RoomManager.getInstance().getFilter().filter(message);

            if (filterResult.isBlocked()) {
                filterResult.sendLogToStaffs(client, "<Whisper: " + playerEntity.getRoom().getData().getId() + ">");
                client.send(new AdvancedAlertMessageComposer(Locale.get("game.message.blocked").replace("%s", filterResult.getMessage())));
                client.getLogger().info("Filter detected a blacklisted word in message: \"" + message + "\"");
                return;
            } else if (filterResult.wasModified()) {
                filteredMessage = filterResult.getMessage();
            }

            filteredMessage = playerEntity.getRoom().getFilter().filter(playerEntity, filteredMessage);
        }

        if (playerEntity.onChat(filteredMessage)) {
            try {
                if (LogManager.ENABLED)
                    LogManager.getInstance().getStore().getLogEntryContainer().put(new RoomChatLogEntry(room.getId(), client.getPlayer().getId(), Locale.getOrDefault("game.logging.whisper", "<Whisper to %username%>").replace("%username%", user) + " " + message));
            } catch (Exception ignored) {

            }

            if (!userTo.getPlayer().ignores(client.getPlayer().getId()))
                userTo.getPlayer().getSession().send(new WhisperMessageComposer(playerEntity.getId(), filteredMessage));

            for (PlayerEntity entity : playerEntity.getRoom().getEntities().getWhisperSeers()) {
                if (entity.getPlayer().getId() != client.getPlayer().getId() && !user.equals(entity.getUsername()))
                    entity.getPlayer().getSession().send(new WhisperMessageComposer(playerEntity.getId(), Locale.getOrDefault("game.whispering", "Whisper to %username%: %message%")));
            }
        }

        client.send(new WhisperMessageComposer(playerEntity.getId(), filteredMessage));
        playerEntity.postChat(message);
    }
}