package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.quests.types.QuestType;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.game.rooms.filter.FilterResult;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import org.apache.commons.lang3.StringUtils;


public class ChangeMottoMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        String motto = msg.readString();

        if (!client.getPlayer().getPermissions().getRank().roomFilterBypass()) {
            FilterResult filterResult = RoomManager.getInstance().getFilter().filter(motto);

            if (filterResult.isBlocked()) {
                client.send(new AdvancedAlertMessageComposer(Locale.get("game.message.blocked").replace("%s", filterResult.getMessage())));
                return;
            } else if (filterResult.wasModified()) {
                motto = filterResult.getMessage();
            }
        }

        client.getPlayer().getData().setMotto(StringUtils.abbreviate(motto, 38));
        client.getPlayer().getData().save();

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new UpdateInfoMessageComposer(client.getPlayer().getEntity()));
        client.send(new UpdateInfoMessageComposer(-1, client.getPlayer().getEntity()));

        client.getPlayer().getAchievements().progressAchievement(AchievementType.MOTTO, 1);
        client.getPlayer().getQuests().progressQuest(QuestType.PROFILE_CHANGE_MOTTO);
    }
}
