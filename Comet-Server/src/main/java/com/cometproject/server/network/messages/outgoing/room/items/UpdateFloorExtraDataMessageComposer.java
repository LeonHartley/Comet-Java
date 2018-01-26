//package com.cometproject.server.network.messages.outgoing.room.items;
//
//import com.cometproject.api.networking.messages.IComposer;
//import com.cometproject.server.game.groups.GroupManager;
//import com.cometproject.server.game.groups.types.GroupData;
//import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
//import com.cometproject.server.game.rooms.objects.items.types.floor.boutique.MannequinFloorItem;
//import com.cometproject.server.game.rooms.objects.items.types.floor.groups.GroupFloorItem;
//import com.cometproject.server.game.rooms.objects.items.types.floor.wired.highscore.HighscoreClassicFloorItem;
//import com.cometproject.server.network.messages.composers.MessageComposer;
//import com.cometproject.server.protocol.headers.Composers;
//import com.cometproject.server.utilities.attributes.Stateable;
//
//
//public class UpdateFloorExtraDataMessageComposer extends MessageComposer {
//    private final int id;
//    private final RoomItemFloor floorItem;
//    private final boolean useGroupItem;
//
//    public UpdateFloorExtraDataMessageComposer(int id, RoomItemFloor floorItem, boolean useGroupItem) {
//        this.id = id;
//        this.floorItem = floorItem;
//        this.useGroupItem = useGroupItem;
//    }
//
//    public UpdateFloorExtraDataMessageComposer(int id, RoomItemFloor floorItem) {
//        this(id, floorItem, true);
//    }
//
//    @Override
//    public short getId() {
//        return Composers.Update;
//    }
//
//    @Override
//    public void compose(IComposer msg) {
//        if (floorItem instanceof MannequinFloorItem) {
//            msg.writeString(String.valueOf(id));
//
//            msg.writeInt(1);
//            msg.writeInt(3);
//
//            msg.writeString("FIGURE");
//            msg.writeString(((MannequinFloorItem) floorItem).getFigure());
//            msg.writeString("GENDER");
//            msg.writeString(((MannequinFloorItem) floorItem).getGender());
//            msg.writeString("OUTFIT_NAME");
//            msg.writeString(((MannequinFloorItem) floorItem).getName());
//        } else if (floorItem instanceof GroupFloorItem && useGroupItem) {
//            GroupData groupData = GroupManager.getInstance().getData(((GroupFloorItem) floorItem).getGroupId());
//
//            msg.writeString(id);
//
//            msg.writeInt(0);
//            if (groupData == null) {
//                msg.writeInt(0);
//            } else {
//                msg.writeInt(2);
//                msg.writeInt(5);
//                msg.writeString(groupData.getTitle());
//                msg.writeString(flooritem.getItemData().getData());
//                msg.writeString(groupData.getBadge());
//
//                String colourA = GroupManager.getInstance().getGroupItems().getSymbolColours().get(groupData.getColourA()).getColour();
//                String colourB = GroupManager.getInstance().getGroupItems().getBackgroundColours().get(groupData.getColourB()).getColour();
//
//                msg.writeString(colourA);
//                msg.writeString(colourB);
//            }
//        } else if(floorItem instanceof HighscoreClassicFloorItem) {
//            msg.writeString(id);
//
//            ((HighscoreClassicFloorItem) floorItem).composeHighscoreData(msg);
//        } else {
//            msg.writeString(id);
//            msg.writeInt(0);
//            msg.writeString(floorItem instanceof Stateable ? (((Stateable) floorItem).getState() ? "1" : "0") : flooritem.getItemData().getData());
//        }
//    }
//}
