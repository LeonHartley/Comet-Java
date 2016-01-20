package com.cometproject.server.game.rooms.objects.items.types;

import com.cometproject.server.game.quests.types.QuestType;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;


public class GenericFloorItem extends RoomItemFloor {
    public GenericFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if (!isWiredTrigger) {
            if (!(entity instanceof PlayerEntity)) {
                return false;
            }

            PlayerEntity pEntity = (PlayerEntity) entity;

            if (this.getDefinition().requiresRights()) {
                if (!pEntity.getRoom().getRights().hasRights(pEntity.getPlayerId())
                        && !pEntity.getPlayer().getPermissions().getRank().roomFullControl()) {
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
    public void onEntityStepOn(GenericEntity entity) {
        if (entity instanceof PlayerEntity) {
            try {
                ((PlayerEntity) entity).getPlayer().getQuests().progressQuest(QuestType.EXPLORE_FIND_ITEM, this.getDefinition().getSpriteId());
            } catch (Exception ignored) {
            }
        }
    }
}
