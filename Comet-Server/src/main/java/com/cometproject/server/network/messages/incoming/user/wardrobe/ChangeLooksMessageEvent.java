package com.cometproject.server.network.messages.incoming.user.wardrobe;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.quests.types.QuestType;
import com.cometproject.server.game.utilities.validator.PlayerFigureValidator;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AlertMessageComposer;
import com.cometproject.server.network.messages.outgoing.user.details.AvatarAspectUpdateMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class ChangeLooksMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        String gender = msg.readString();
        String figure = msg.readString();

        if(figure == null) return;

        if (!PlayerFigureValidator.isValidFigureCode(figure, gender.toLowerCase())) {
            client.send(new AlertMessageComposer(Locale.getOrDefault("game.figure.invalid", "That figure is invalid!")));
            return;
        }

        if (!gender.toLowerCase().equals("m") && !gender.toLowerCase().equals("f")) {
            return;
        }

        int timeSinceLastUpdate = ((int) Comet.getTime()) - client.getPlayer().getLastFigureUpdate();

        if (timeSinceLastUpdate >= CometSettings.playerChangeFigureCooldown) {
            client.getPlayer().getData().setGender(gender);
            client.getPlayer().getData().setFigure(figure);
            client.getPlayer().getData().save();

            client.getPlayer().poof();
            client.getPlayer().setLastFigureUpdate((int) Comet.getTime());
        }

        client.getPlayer().getAchievements().progressAchievement(AchievementType.AVATAR_LOOKS, 1);
        client.getPlayer().getQuests().progressQuest(QuestType.PROFILE_CHANGE_LOOK);
        client.send(new AvatarAspectUpdateMessageComposer(figure, gender));
    }
}
