package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class UpdateInfoMessageComposer {
    public static Composer compose(int userId, String figure, String gender, String motto, int achievementPoints) {
        Composer msg = new Composer(Composers.UpdateUserDataMessageComposer);

        System.out.println(userId);

        msg.writeInt(userId);
        msg.writeString(figure);
        msg.writeString(gender.toLowerCase());
        msg.writeString(motto);
        msg.writeInt(achievementPoints);

        return msg;
    }

    public static Composer compose(GenericEntity entity) {
        return compose(entity.getId(), entity.getFigure(), entity.getGender(), entity.getMotto(), (entity instanceof PlayerEntity) ? ((PlayerEntity) entity).getPlayer().getData().getAchievementPoints() : 0);
    }

    public static Composer compose(boolean isMe, GenericEntity entity) {
        if (!isMe) {
            return compose(entity.getId(), entity.getFigure(), entity.getGender(), entity.getMotto(), (entity instanceof PlayerEntity) ? ((PlayerEntity) entity).getPlayer().getData().getAchievementPoints() : 0);
        } else {
            return compose(-1, entity.getFigure(), entity.getGender(), entity.getMotto(), (entity instanceof PlayerEntity) ? ((PlayerEntity) entity).getPlayer().getData().getAchievementPoints() : 0);
        }
    }
}
