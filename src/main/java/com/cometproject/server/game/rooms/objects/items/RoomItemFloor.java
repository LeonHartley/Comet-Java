package com.cometproject.server.game.rooms.objects.items;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.objects.items.data.BackgroundTonerData;
import com.cometproject.server.game.rooms.objects.items.types.floor.MagicStackFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.boutique.MannequinFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.football.FootballGateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.AbstractWiredItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorExtraDataMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public abstract class RoomItemFloor extends RoomItem {
    private String extraData;

    private ItemDefinition itemDefinition;

    public RoomItemFloor(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, new Position(x, y, z), room);

        this.itemId = itemId;
        this.ownerId = owner;
        this.rotation = rotation;
        this.extraData = data;
    }

    public void serialize(Composer msg, boolean isNew) {
        //final boolean isGift = false;

        /*if (this.giftData != null) {
            isGift = true;
        }*/

        // TODO: MOVE SPECIAL PROPERTIES TO THE INDIVIDUAL ITEM CLASS!!!

        msg.writeInt(this.getId());
        //msg.writeInt(isGift ? giftData.getSpriteId() : this.getDefinition().getSpriteId());
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeInt(this.getPosition().getX());
        msg.writeInt(this.getPosition().getY());
        msg.writeInt(this.getRotation());

        msg.writeString(this instanceof MagicStackFloorItem ? this.getExtraData() : Double.toString(this.getPosition().getZ()));
        msg.writeString(Double.toString(this.getPosition().getZ()));

        if (this.getDefinition().isAdFurni()) {
            msg.writeInt(0);
            msg.writeInt(1);

            if (!extraData.equals("")) {
                String[] adsData = extraData.split(String.valueOf((char) 9));
                int count = adsData.length;

                msg.writeInt(count / 2);

                for (int i = 0; i <= count - 1; i++) {
                    msg.writeString(adsData[i]);
                }
            } else {
                msg.writeInt(0);
            }
        } else if (this.getDefinition().getInteraction().equals("badge_display")) {
            msg.writeInt(0);
            msg.writeInt(2);
            msg.writeInt(4);

            msg.writeString("0");
            msg.writeString(extraData);
            msg.writeString("");
            msg.writeString("");


        } else if (this.getDefinition().getInteraction().equals("mannequin")) {
            msg.writeInt(0);
            msg.writeInt(1);
            msg.writeInt(3);

            msg.writeString("GENDER");
            msg.writeString(((MannequinFloorItem) this).getGender());
            msg.writeString("FIGURE");
            msg.writeString(((MannequinFloorItem) this).getFigure());
            msg.writeString("OUTFIT_NAME");
            msg.writeString(((MannequinFloorItem) this).getName());

            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(this.ownerId);

            if (isNew) {
                msg.writeString(this.getRoom().getData().getOwner());
            }

            return;
        } else if (this.getDefinition().getInteraction().equals("roombg")) {
            BackgroundTonerData data = BackgroundTonerData.get(extraData);

            boolean enabled = (data != null);

            msg.writeInt(0);
            msg.writeInt(5);
            msg.writeInt(4);
            msg.writeInt(enabled ? 1 : 0);

            if (enabled) {
                msg.writeInt(data.getHue());
                msg.writeInt(data.getSaturation());
                msg.writeInt(data.getLightness());
            } else {
                this.extraData = "0;#;0;#;0";
                this.saveData();

                msg.writeInt(0);
                msg.writeInt(0);
                msg.writeInt(0);
            }
        } else if (this.getDefinition().getItemName().contains("yttv") && this.hasAttribute("video")) {
            msg.writeInt(0);
            msg.writeInt(1);
            msg.writeInt(1);
            msg.writeString("THUMBNAIL_URL");
            msg.writeString("/deliver/" + this.getAttribute("video"));
        } else if (this instanceof GroupFloorItem) {
            GroupData groupData = CometManager.getGroups().getData(((GroupFloorItem) this).getGroupId());

            msg.writeInt(0);
            if (groupData == null) {
                msg.writeInt(0);
            } else {
                msg.writeInt(2);
                msg.writeInt(5);
                msg.writeString("0");
                msg.writeString(this.getExtraData());
                msg.writeString(groupData.getBadge());

                String colourA = CometManager.getGroups().getGroupItems().getSymbolColours().get(groupData.getColourA()).getColour();
                String colourB = CometManager.getGroups().getGroupItems().getBackgroundColours().get(groupData.getColourB()).getColour();

                msg.writeString(colourA);
                msg.writeString(colourB);
            }

        } else {
            msg.writeInt(0);
            msg.writeInt(0);

            //msg.writeString(isGift ? giftData.toString() : this.getExtraData());
            msg.writeString((this instanceof FootballGateFloorItem) ? "" : this.getExtraData());

            //msg.writeInt(15); // rare id
            //msg.writeInt(100); // amount of limited items in a stack
        }

        msg.writeInt(-1);
        //msg.writeInt(!this.getDefinition().getInteraction().equals("default") ? 1 : 0);
        msg.writeInt(!this.getDefinition().getInteraction().equals("default") ? 1 : 0);
        msg.writeInt(this.getRoom().getData().getOwnerId());

        if (isNew)
            msg.writeString(this.getRoom().getData().getOwner());
    }

    @Override
    public void serialize(Composer msg) {
        this.serialize(msg, false);
    }

    public ItemDefinition getDefinition() {
        if (this.itemDefinition == null) {
            this.itemDefinition = CometManager.getItems().getDefinition(this.getItemId());
        }

        return this.itemDefinition;
    }

    public void onItemAddedToStack(RoomItemFloor floorItem) {

    }

    public void onEntityPreStepOn(GenericEntity entity) {

    }

    public void onEntityStepOn(GenericEntity entity) {

    }

    public void onEntityStepOff(GenericEntity entity) {
    }

    @Override
    public boolean toggleInteract(boolean state) {
        if (!state) {
            if (!(this instanceof AbstractWiredItem))
                this.setExtraData("0");

            return true;
        }

        if(!StringUtils.isNumeric(this.getExtraData())) {
            return true;
        }

        if (this.getDefinition().getInteractionCycleCount() > 1) {
            if (this.getExtraData().isEmpty() || this.getExtraData().equals(" ")) {
                this.setExtraData("0");
            }

            int i = Integer.parseInt(this.getExtraData()) + 1;

            if (i > (this.getDefinition().getInteractionCycleCount() - 1)) { // take one because count starts at 0 (0, 1) = count(2)
                this.setExtraData("0");
            } else {
                this.setExtraData(i + "");
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void saveData() {
        RoomItemDao.saveData(this.getId(), this.getDataObject());
    }

    @Override
    public void sendUpdate() {
        Room r = this.getRoom();

        if (r != null) {
            r.getEntities().broadcastMessage(UpdateFloorExtraDataMessageComposer.compose(this.getId(), this));
        }
    }

    public List<RoomItemFloor> getItemsOnStack() {
        List<RoomItemFloor> floorItems = Lists.newArrayList();

        List<AffectedTile> affectedTiles = AffectedTile.getAffectedTilesAt(
                this.getDefinition().getLength(), this.getDefinition().getWidth(), this.getPosition().getX(), this.getPosition().getY(), this.getRotation());

        floorItems.addAll(this.getRoom().getItems().getItemsOnSquare(this.getPosition().getX(), this.getPosition().getY()));

        for (AffectedTile tile : affectedTiles) {
            for (RoomItemFloor floorItem : this.getRoom().getItems().getItemsOnSquare(tile.x, tile.y)) {
                if (!floorItems.contains(floorItem)) floorItems.add(floorItem);
            }
        }

        return floorItems;
    }

    public List<GenericEntity> getEntitiesOnItem() {
        List<GenericEntity> entities = Lists.newArrayList();

        entities.addAll(this.getRoom().getEntities().getEntitiesAt(this.getPosition().getX(), this.getPosition().getY()));

        for (AffectedTile affectedTile : AffectedTile.getAffectedTilesAt(this.getDefinition().getLength(), this.getDefinition().getWidth(), this.getPosition().getX(), this.getPosition().getY(), this.getRotation())) {
            List<GenericEntity> entitiesOnTile = this.getRoom().getEntities().getEntitiesAt(affectedTile.x, affectedTile.y);

            entities.addAll(entitiesOnTile);
        }

        return entities;
    }

    public Position getPartnerTile() {
        if(this.getDefinition().getLength() != 2) return null;

        for (AffectedTile affTile : AffectedTile.getAffectedBothTilesAt(this.getDefinition().getLength(), this.getDefinition().getWidth(), this.getPosition().getX(), this.getPosition().getY(), this.getRotation())) {
            if(affTile.x == this.getPosition().getX() && affTile.y == this.getPosition().getY()) continue;

            return new Position(affTile.x, affTile.y);
        }

        return null;
    }

    public double getOverrideHeight() {
        return -1d;
    }

    public String getDataObject() {
        return this.extraData;
    }

    public String getExtraData() {
        return this.extraData;
    }

    public void setRotation(int rot) {
        this.rotation = rot;
    }

    public void setExtraData(String data) {
        this.extraData = data;
    }
}
