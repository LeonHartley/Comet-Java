package com.cometproject.server.network.messages.incoming.user.wardrobe;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.quests.QuestType;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class ChangeLooksMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        String gender = msg.readString();
        String figure = msg.readString();

        if (!gender.toLowerCase().equals("m") && !gender.toLowerCase().equals("f")) {
            return;
        }

//        if(!this.isValidFigure(figure)) {
//            client.send(new MotdNotificationComposer("Invalid figure, bro!"));
//            return;
//        }

        int timeSinceLastUpdate = ((int) Comet.getTime()) - client.getPlayer().getLastFigureUpdate();

        if (timeSinceLastUpdate >= CometSettings.playerFigureUpdateTimeout) {
            client.getPlayer().getData().setGender(gender);
            client.getPlayer().getData().setFigure(figure);
            client.getPlayer().getData().save();

            client.getPlayer().poof();
            client.getPlayer().setLastFigureUpdate((int) Comet.getTime());
        }

        client.getPlayer().getAchievements().progressAchievement(AchievementType.AVATAR_LOOKS, 1);
        client.getPlayer().getQuests().progressQuest(QuestType.PROFILE_CHANGE_LOOK);
    }

    private boolean isValidFigure(String figure) {
        boolean hasHead = false;

        if (figure.length() < 1)
            return false;

        try {
            String[] sets = figure.split(".");

            if (sets.length < 4)
                return false;

            for (String set : sets) {
                String[] parts = set.split("-");

                if (parts.length < 3) {
                    return false;
                }

                String name = parts[0];
                int type = Integer.parseInt(parts[1]);
                int colour = Integer.parseInt(parts[2]);

                if (type <= 0 || colour < 0) {
                    return false;
                }

                if (name.length() != 2) {
                    return false;
                }

                if (name.equals("hd")) {
                    hasHead = true;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return hasHead;
    }
}
