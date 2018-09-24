package com.cometproject.server.game.rooms.objects.items.types;

import com.cometproject.api.game.quests.QuestType;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;


public class DefaultFloorItem extends RoomItemFloor {
    public DefaultFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public boolean onInteract(RoomEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            if (!(entity instanceof PlayerEntity)) {
                return false;
            }

            PlayerEntity pEntity = (PlayerEntity) entity;

            if (this.getDefinition().requiresRights()) {
                if (!pEntity.getRoom().getRights().hasRights(pEntity.getPlayerId()) && !pEntity.getPlayer().getPermissions().getRank().roomFullControl()) {
                    return false;
                }
            }

            if (pEntity.getPlayer().getId() == this.getRoom().getData().getOwnerId()) {
                pEntity.getPlayer().getQuests().progressQuest(QuestType.FURNI_SWITCH);
            }
        }

        this.toggleInteract(true);
        this.sendUpdate();

        this.saveData();
        return true;
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (entity instanceof PlayerEntity) {
            try {
                ((PlayerEntity) entity).getPlayer().getQuests().progressQuest(QuestType.EXPLORE_FIND_ITEM, this.getDefinition().getSpriteId());
            } catch (Exception ignored) {
            }
        }
    }
}
