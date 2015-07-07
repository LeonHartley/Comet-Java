package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.entries.RoomChatLogEntry;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.TalkMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.google.common.primitives.Ints;


public class TalkMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        String message = msg.readString();
        int colour = msg.readInt();

        PlayerEntity playerEntity = client.getPlayer().getEntity();

        if (playerEntity == null || playerEntity.getRoom() == null || playerEntity.getRoom().getEntities() == null)
            return;

        if (!TalkMessageEvent.isValidColour(colour, client))
            colour = 0;

        String filteredMessage = filterMessage(message);

        if (!client.getPlayer().getPermissions().hasPermission("bypass_filter")) {
            FilterResult filterResult = RoomManager.getInstance().getFilter().filter(message);

            if (filterResult.isBlocked()) {
                client.send(new AdvancedAlertMessageComposer(Locale.get("game.message.blocked").replace("%s", filterResult.getMessage())));
                return;
            } else if (filterResult.wasModified()) {
                filteredMessage = filterResult.getMessage();
            }
        }

        if (playerEntity.onChat(filteredMessage)) {
            try {
                if (LogManager.ENABLED)
                    LogManager.getInstance().getStore().getLogEntryContainer().put(new RoomChatLogEntry(playerEntity.getRoom().getId(), client.getPlayer().getId(), message));
            } catch (Exception ignored) {

            }

            playerEntity.getRoom().getEntities().broadcastChatMessage(
                    new TalkMessageComposer(
                            playerEntity.getId(),
                            filteredMessage,
                            RoomManager.getInstance().getEmotions().getEmotion(filteredMessage),
                            colour
                    ),

                    playerEntity);
        }

        playerEntity.postChat(filteredMessage);
    }

    private static int[] allowedColours = new int[]{
            0, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 29
    };

    public static boolean isValidColour(int colour, Session client) {
        if (!Ints.contains(allowedColours, colour)) {
            return false;
        }

        if (colour == 23 && !client.getPlayer().getPermissions().hasPermission("mod_tool"))
            return false;

        return true;
    }

    public static String filterMessage(String message) {
        if (message.contains("You can type here to talk!")) {
            message = message.replace("You can type here to talk!", "");
        }

        return message.replace((char) 13 + "", "");
    }
}