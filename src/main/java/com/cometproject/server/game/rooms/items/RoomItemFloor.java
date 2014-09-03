package com.cometproject.server.game.rooms.items;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.groups.types.GroupData;
import com.cometproject.server.game.items.types.ItemDefinition;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.data.BackgroundTonerData;
import com.cometproject.server.game.rooms.items.data.MannequinData;
import com.cometproject.server.game.rooms.items.types.floor.MagicStackFloorItem;
import com.cometproject.server.game.rooms.items.types.floor.groups.GroupFloorItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.UpdateFloorExtraDataMessageComposer;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.rooms.RoomItemDao;

public abstract class RoomItemFloor extends RoomItem {
    private int roomId;
    private double height;
    private String extraData;

    private Room room;

    private ItemDefinition itemDefinition;

    public RoomItemFloor(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        this.init(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    private void init(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        this.id = id;
        this.itemId = itemId;
        this.roomId = roomId;
        this.ownerId = owner;
        this.x = x;
        this.y = y;
        this.height = z;
        this.rotation = rotation;
        this.extraData = data;
    }

    public void serialize(Composer msg, boolean isNew) {
        boolean isGift = false;

        /*if (this.giftData != null) {
            isGift = true;
        }*/

        // TODO: MOVE SPECIAL PROPERTIES TO THE INDIVIDUAL ITEM CLASS!!!

        msg.writeInt(this.getId());
        //msg.writeInt(isGift ? giftData.getSpriteId() : this.getDefinition().getSpriteId());
        msg.writeInt(this.getDefinition().getSpriteId());
        msg.writeInt(this.getX());
        msg.writeInt(this.getY());
        msg.writeInt(this.getRotation());

        msg.writeString(this instanceof MagicStackFloorItem ? this.getExtraData() : Double.toString(this.getHeight()));
        msg.writeString(Double.toString(this.getHeight()));

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
            MannequinData data = MannequinData.get(extraData);

            msg.writeInt(0);
            msg.writeInt(1);
            msg.writeInt(3);

            String gender = "m";
            String figure = "ch-210-62.lg-270-62";
            String name = "New Mannequin";

            if (data != null) {
                gender = data.getGender().toLowerCase();
                figure = data.getFigure();
                name = data.getName();
            }

            msg.writeString("GENDER");
            msg.writeString(gender);
            msg.writeString("FIGURE");
            msg.writeString(figure);
            msg.writeString("OUTFIT_NAME");
            msg.writeString(name);

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
        } else if(this instanceof GroupFloorItem) {
            GroupData groupData = CometManager.getGroups().getData(((GroupFloorItem) this).getGroupId());

            msg.writeInt(0);
            if(groupData == null) {
                msg.writeInt(0);
            } else {
                msg.writeInt(2);
                msg.writeInt(5);
                msg.writeString("0");
                msg.writeString(this.getExtraData());
                msg.writeString(groupData.getBadge());
                msg.writeString(CometManager.getGroups().getGroupItems().getBackgroundColour(groupData.getColourA()));
                msg.writeString(CometManager.getGroups().getGroupItems().getBackgroundColour(groupData.getColourB()));
            }
        } else {
            msg.writeInt(0);
            msg.writeInt(0);

            //msg.writeString(isGift ? giftData.toString() : this.getExtraData());
            msg.writeString(this.getExtraData());

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
        if(this.itemDefinition == null) {
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
        String interaction = this.getDefinition().getInteraction();

        if (!state) {
            if (!CometManager.getWired().isWiredItem(this))
                this.setExtraData("0");

            return true;
        }

        if ((interaction.equals("default") || interaction.equals("gate") || interaction.equals("pressure_pad")) && (this.getDefinition().getInteractionCycleCount() > 1)) {
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

    public Room getRoom() {
        if (this.room == null) {
            this.room = CometManager.getRooms().get(this.roomId);
        }

        return this.room;
    }

    @Override
    public void saveData() {
        RoomItemDao.saveData(id, extraData);
    }

    @Override
    public void sendUpdate() {
        Room r = this.getRoom();

        if (r != null) {
            r.getEntities().broadcastMessage(UpdateFloorExtraDataMessageComposer.compose(this.getId(), this.getExtraData()));
        }
    }

    public double getHeight() {
        return this.height;
    }

    public String getExtraData() {
        return this.extraData;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRotation(int rot) {
        this.rotation = rot;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setExtraData(String data) {
        this.extraData = data;
    }
}
