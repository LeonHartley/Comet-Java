package com.cometproject.server.game.rooms.objects.items.types.floor.groups;

import com.cometproject.api.game.GameContext;
import com.cometproject.api.game.groups.types.IGroupData;
import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.api.game.rooms.objects.data.ItemData;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.game.rooms.objects.data.StringArrayItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.AvatarUpdateMessageComposer;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;


public class GroupFloorItem extends RoomItemFloor {
    private int groupId;

    public GroupFloorItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        final String data = this.getItemData().getData();

        if (!StringUtils.isNumeric(data) || data.isEmpty())
            this.groupId = 0;
        else
            this.groupId = Integer.parseInt(data);
    }


    @Override
    public ItemData createItemData() {
        final IGroupData groupData = GameContext.getCurrent().getGroupService().getData(this.groupId);

        if (groupData == null) {
            return new StringArrayItemData(new String[]{});
        } else {
            final String itemState = this instanceof GroupGateFloorItem ? ((GroupGateFloorItem) this).isOpen ? "1" : "0" : "0";
            final String firstColour = GameContext.getCurrent().getGroupService().getItemService().getSymbolColours().get(groupData.getColourA()) != null ? GameContext.getCurrent().getGroupService().getItemService().getSymbolColours().get(groupData.getColourA()).getFirstValue() : "ffffff";
            final String secondColour = GameContext.getCurrent().getGroupService().getItemService().getBackgroundColours().get(groupData.getColourB()) != null ? GameContext.getCurrent().getGroupService().getItemService().getBackgroundColours().get(groupData.getColourB()).getFirstValue() : "ffffff";

            return new StringArrayItemData(new String[]{
                    itemState, this.getItemData().getData(), groupData.getBadge(),
                    firstColour, secondColour
            });
        }
    }

    public void onEntityStepOn(RoomEntity entity, boolean instantUpdate) {
        if (!this.getDefinition().canSit()) return;

        double height = (entity instanceof PetEntity || entity.hasAttribute("transformation")) ? this.getSitHeight() / 2 : this.getSitHeight();

        entity.setBodyRotation(this.getRotation());
        entity.setHeadRotation(this.getRotation());
        entity.addStatus(RoomEntityStatus.SIT, String.valueOf(height).replace(',', '.'));

        if (instantUpdate)
            this.getRoom().getEntities().broadcastMessage(new AvatarUpdateMessageComposer(entity));
        else
            entity.markNeedsUpdate();
    }

    @Override
    public String getDataObject() {
        return this.groupId + "";
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        this.onEntityStepOn(entity, false);
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if (entity.hasStatus(RoomEntityStatus.SIT)) {
            entity.removeStatus(RoomEntityStatus.SIT);
        }

        entity.markNeedsUpdate();
    }

    public double getSitHeight() {
        return this.getDefinition().getHeight();
    }

    public int getGroupId() {
        return groupId;
    }
}
