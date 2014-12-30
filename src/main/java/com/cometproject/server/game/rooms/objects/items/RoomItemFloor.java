package com.cometproject.server.game.rooms.objects.items;

import com.cometproject.server.game.catalog.types.gifts.GiftData;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.players.data.PlayerData;
import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.pathfinding.AffectedTile;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.data.BackgroundTonerData;
import com.cometproject.server.game.rooms.objects.items.types.floor.GiftFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.MagicStackFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.boutique.MannequinFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.football.FootballGateFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupFloorItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.AbstractWiredItem;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorExtraDataMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;
import com.cometproject.server.utilities.attributes.Collidable;
import com.cometproject.server.utilities.comporators.PositionComporator;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;


public abstract class RoomItemFloor extends RoomItem implements Collidable {
    private String extraData;

    private ItemDefinition itemDefinition;
    private GenericEntity collidedEntity;

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
        // This needs cleaning up immensely.

        msg.writeInt(this.getId());
        //msg.writeInt(isGift ? giftData.getSpriteId() : this.getDefinition().getSpriteId());
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeInt(this.getPosition().getX());
        msg.writeInt(this.getPosition().getY());
        msg.writeInt(this.getRotation());

        msg.writeString(this instanceof MagicStackFloorItem ? this.getExtraData() : Double.toString(this.getPosition().getZ()));
        msg.writeString(Double.toString(this.getPosition().getZ()));

        if (this instanceof GiftFloorItem) {
            final GiftData giftData = ((GiftFloorItem) this).getGiftData();
            final PlayerData purchaser = PlayerDao.getDataById(giftData.getSenderId());

            msg.writeInt(giftData.getWrappingPaper() * 1000 + giftData.getDecorationType());
            msg.writeInt(1);
            msg.writeInt(6);
            msg.writeString("EXTRA_PARAM");
            msg.writeString("");
            msg.writeString("MESSAGE");
            msg.writeString(giftData.getMessage());
            msg.writeString("PURCHASER_NAME");
            msg.writeString(purchaser.getUsername());
            msg.writeString("PURCHASER_FIGURE");
            msg.writeString(purchaser.getFigure());
            msg.writeString("PRODUCT_CODE");
            msg.writeString("");
            msg.writeString("state");
            msg.writeString(((GiftFloorItem) this).isOpened() ? "1" : "0");
        } else if (this.getDefinition().isAdFurni()) {
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
            GroupData groupData = GroupManager.getInstance().getData(((GroupFloorItem) this).getGroupId());

            msg.writeInt(0);
            if (groupData == null) {
                msg.writeInt(0);
            } else {
                msg.writeInt(2);
                msg.writeInt(5);
                msg.writeString("0");
                msg.writeString(this.getExtraData());
                msg.writeString(groupData.getBadge());

                String colourA = GroupManager.getInstance().getGroupItems().getSymbolColours().get(groupData.getColourA()) != null ? GroupManager.getInstance().getGroupItems().getSymbolColours().get(groupData.getColourA()).getColour() : "ffffff";
                String colourB = GroupManager.getInstance().getGroupItems().getBackgroundColours().get(groupData.getColourB()) != null ? GroupManager.getInstance().getGroupItems().getBackgroundColours().get(groupData.getColourB()).getColour() : "ffffff";

                msg.writeString(colourA);
                msg.writeString(colourB);
            }

        } else if (this.getLimitedEditionItem() != null) {
            msg.writeInt(0);
            msg.writeString("");
            msg.writeBoolean(true);
            msg.writeBoolean(false);
            msg.writeString(this.getExtraData());

            msg.writeInt(this.getLimitedEditionItem().getLimitedRare());
            msg.writeInt(this.getLimitedEditionItem().getLimitedRareTotal());
        } else {
            msg.writeInt(0);
            msg.writeInt(0);

            //msg.writeString(isGift ? giftData.toString() : this.getExtraData());
            msg.writeString((this instanceof FootballGateFloorItem) ? "" : this.getExtraData());
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
            this.itemDefinition = ItemManager.getInstance().getDefinition(this.getItemId());
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

        if (!StringUtils.isNumeric(this.getExtraData())) {
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

        entities.addAll(this.getRoom().getEntities().getEntitiesAt(this.getPosition()));

        for (AffectedTile affectedTile : AffectedTile.getAffectedTilesAt(this.getDefinition().getLength(), this.getDefinition().getWidth(), this.getPosition().getX(), this.getPosition().getY(), this.getRotation())) {
            List<GenericEntity> entitiesOnTile = this.getRoom().getEntities().getEntitiesAt(new Position(affectedTile.x, affectedTile.y));

            entities.addAll(entitiesOnTile);
        }

        return entities;
    }

    public Position getPartnerTile() {
        if (this.getDefinition().getLength() != 2) return null;

        for (AffectedTile affTile : AffectedTile.getAffectedBothTilesAt(this.getDefinition().getLength(), this.getDefinition().getWidth(), this.getPosition().getX(), this.getPosition().getY(), this.getRotation())) {
            if (affTile.x == this.getPosition().getX() && affTile.y == this.getPosition().getY()) continue;

            return new Position(affTile.x, affTile.y);
        }

        return null;
    }

    public PlayerEntity nearestPlayerEntity() {
        PositionComporator positionComporator = new PositionComporator(this);

        List<PlayerEntity> nearestEntities = this.getRoom().getEntities().getPlayerEntities();

        Collections.sort(nearestEntities, positionComporator);

        for (PlayerEntity playerEntity : nearestEntities) {
//            if(playerEntity.getTile().isReachable(this)) {
            return playerEntity;
//            }
        }

        return null;
    }

    public GenericEntity getCollision() {
        return this.collidedEntity;
    }

    public void setCollision(GenericEntity entity) {
        this.collidedEntity = entity;
    }

    public void nullifyCollision() {
        this.collidedEntity = null;
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
