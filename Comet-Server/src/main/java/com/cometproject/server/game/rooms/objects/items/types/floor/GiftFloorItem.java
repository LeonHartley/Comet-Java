package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.furniture.types.GiftData;
import com.cometproject.api.game.players.data.PlayerAvatar;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.players.PlayerManager;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.items.RemoveFloorItemMessageComposer;


public class GiftFloorItem extends RoomItemFloor {
    private GiftData giftData;
    private boolean isOpened = false;

    public GiftFloorItem(RoomItemData roomItemData, Room room) throws Exception {
        super(roomItemData, room);

        this.giftData = JsonUtil.getInstance().fromJson(roomItemData.getData().split("GIFT::##")[1], GiftData.class);

        if (!CatalogManager.getInstance().getGiftBoxesNew().contains(giftData.getSpriteId()) && !CatalogManager.getInstance().getGiftBoxesOld().contains(giftData.getSpriteId())) {
            throw new Exception("some sad fucker used an exploit, bye bye gift.");
        }
    }

    @Override
    public void composeItemData(IComposer msg) {
        final GiftData giftData = this.getGiftData();
        final PlayerAvatar purchaser = PlayerManager.getInstance().getAvatarByPlayerId(giftData.getSenderId(), PlayerAvatar.USERNAME_FIGURE);

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
        msg.writeString(this.isOpened() ? "1" : "0");
    }

    @Override
    public boolean onInteract(RoomEntity entity, int state, boolean isWiredTrigger) {
        this.isOpened = true;

        this.sendUpdate();
//        this.getRoom().getEntities().broadcastMessage(new RemoveFloorItemMessageComposer(this.getVirtualId(), this.getItemData().getOwnerId(), 1200));
//        this.getRoom().getEntities().broadcastMessage(new SendFloorItemMessageComposer(this));

        this.isOpened = false;
        return true;
    }

    public GiftData getGiftData() {
        return giftData;
    }

    public boolean isOpened() {
        return isOpened;
    }
}
